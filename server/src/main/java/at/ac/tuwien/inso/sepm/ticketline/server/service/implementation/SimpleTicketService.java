package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketSeatStatus;
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.ForbiddenException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.SeatSelectionException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.ServerServiceValidationException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.*;
import at.ac.tuwien.inso.sepm.ticketline.server.service.InvoiceService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ReservationNumberService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class SimpleTicketService implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketSeatRepository ticketSeatRepository;
    private final CustomerRepository customerRepository;
    private final SeatRepository seatRepository;
    private final PerformanceRepository performanceRepository;
    private final ReservationNumberService reservationNumberService;
    private final InvoiceService invoiceService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleTicketService.class);

    public SimpleTicketService(TicketRepository ticketRepository, TicketSeatRepository ticketSeatRepository,
                               CustomerRepository customerRepository, SeatRepository seatRepository,
                               PerformanceRepository performanceRepository,
                               ReservationNumberService reservationNumberService,
                               InvoiceService invoiceService) {
        this.ticketRepository = ticketRepository;
        this.ticketSeatRepository = ticketSeatRepository;
        this.customerRepository = customerRepository;
        this.seatRepository = seatRepository;
        this.performanceRepository = performanceRepository;
        this.reservationNumberService = reservationNumberService;
        this.invoiceService = invoiceService;
    }

    @Override
    public Page<Ticket> findAll(Pageable request) {
        LOGGER.info("Find all tickets");
        return ticketRepository.findAllByStatusIsNot(TicketStatus.RC,request);
    }

    @Override
    public Ticket findOne(Long id) {
        return ticketRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public Page<Ticket> findByKeyword(String keyword, Pageable request) {
        LOGGER.info("Find tickets by filter");
        return ticketRepository.findByreservationNumberContainingOrCustomer_FirstnameOrCustomer_SurnameContainingAllIgnoreCase(keyword,keyword,keyword,request);
    }

    @Override
    public Ticket cancelTicket(Ticket ticket) {
        LOGGER.info("Cancel a ticket by id " + ticket.getId());
        if(ticket.getStatus() == TicketStatus.IC || ticket.getStatus() == TicketStatus.RC || ticket.getPerformance().getStartDateTime().isBefore(LocalDateTime.now())){
            throw new ServerServiceValidationException("Ticket is already canceled!");
        }

        List<TicketSeat> list = ticketSeatRepository.findTicketSeatsByTicket_Id(ticket.getId());
        for (TicketSeat ts:list) {
            ticketSeatRepository.updateTicketSeatStatus(ts.getId());
        }

        ticketRepository.updateTicketStatus((ticket.getStatus() == TicketStatus.R ? TicketStatus.RC : TicketStatus.IC).toString(),ticket.getId());

        performanceRepository.updateSeatCapacity(ticket.getPerformance().getId());
        return ticket;
    }


    //Saves or updates a given ticket
    @Override
    public Ticket saveTicket(Ticket ticket) {
        LOGGER.info("Save a ticket by id " + ticket.getId());
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket saveToAnonymous(Long performance_id, List<Long> seat_ids) throws SeatSelectionException {
        LOGGER.info("Reserve tickets to anonymous");
        //First save the ticket, fetch the generated id value
        Customer anonymous = customerRepository.findOneById(1L).orElseThrow(NotFoundException::new);
        Performance performance = performanceRepository.findOneById(performance_id).orElseThrow(NotFoundException::new);
        if(performance.getStartDateTime().isBefore(LocalDateTime.now())){
            throw new ForbiddenException();
        }
        List<Seat> selectedSeats = seatRepository.findAllById(seat_ids);
        if(selectedSeats.size() != seat_ids.size()){
            throw new NotFoundException();
        }

        //calculate the subtotal of this ticket
        Double price = 0d;
        for(Seat s : selectedSeats){
            price += s.getMultiplier()*performance.getBasePrice();
        }
        price = Math.round(price*100.0)/100.0;

        //Create and save ticket first
        Ticket ticket = Ticket.builder()
            .status(TicketStatus.T)
            .dateOfIssue(LocalDateTime.now())
            .customer(anonymous)
            .price(price)
            .performance(performance)
            .reservationNumber(reservationNumberService.save(new ReservationNumber()))
            .build();

        //Update ticket variable: After saving it received an id
        ticket = ticketRepository.save(ticket);

        //generate one ticket seat, fill in later
        TicketSeat.TicketSeatBuilder builder = TicketSeat.builder()
            .performance(performance)
            .ticket(ticket)
            .status(TicketSeatStatus.RESERVED);

        controlSeatsAndInsert(selectedSeats, performance_id, builder);

        return ticket;
    }

    @Override
    public Ticket update(Long ticket_id, List<Long> seat_ids) throws SeatSelectionException {
        LOGGER.info("Update a ticket by id " + ticket_id);

        Ticket ticket = ticketRepository.getOne(ticket_id);
        if(ticket.getPerformance().getStartDateTime().isBefore(LocalDateTime.now())){
            throw new ForbiddenException();
        }

        if(ticket.getStatus().equals(TicketStatus.IC) || ticket.getStatus().equals(TicketStatus.RC)){
            throw new ServerServiceValidationException("Ticket has already been cancelled");
        }

        List<Seat> selectedSeats = seatRepository.findAllById(seat_ids);
        List<TicketSeat> formerlySelectedSeats = new ArrayList<>();
        ticketSeatRepository.findTicketSeatsByTicket_Id(ticket_id).forEach(s -> {formerlySelectedSeats.add(s); });

        Double price = 0d;

        //calculate the subtotal of this ticket
        for(Seat s : selectedSeats){
            price += s.getMultiplier()*ticket.getPerformance().getBasePrice();
        }

        price = Math.round(price*100.0)/100.0;
        ticket.setPrice(price);

        //Delete all formerly selected seats
        ticketSeatRepository.deleteAll(formerlySelectedSeats);

        TicketSeat.TicketSeatBuilder builder = TicketSeat.builder()
            .performance(ticket.getPerformance())
            .ticket(ticket)
            .status(TicketSeatStatus.RESERVED);

        controlSeatsAndInsert(selectedSeats, ticket.getPerformance().getId(), builder);

        return ticketRepository.save(ticket);
    }

    private void controlSeatsAndInsert(List<Seat> selectedSeats, Long performance_id, TicketSeat.TicketSeatBuilder builder) throws SeatSelectionException{
        for(Seat s : selectedSeats){
            Seat temp = seatRepository.isSeatAvailable(performance_id, s.getId());
            if(temp != null){
                //seat taken
                if(s.getType().equals(SeatType.STAND)){
                    //find one available seat from its sector
                    temp = seatRepository.getOneAvailableFromSectorStanding(s.getSector(), performance_id, s.getId(), s.getHall().getId());
                    if(temp == null){
                        throw new SeatSelectionException("Not enough seats available in sector: " + s.getSector());
                    }else{
                        ticketSeatRepository.save(builder.seat(temp).build());
                    }
                }else{
                    //ordinary seat
                    throw new SeatSelectionException("The seat " + s.getRow()+":"+s.getNumber() + " has already been taken");
                }
            }else{
                //seat not taken, add to the database
                ticketSeatRepository.save(builder.seat(s).build());
            }
        }
        performanceRepository.updateSeatCapacity(performance_id);
    }

    @Override
    public Ticket reserve(Ticket ticket) {
        LOGGER.info("Reserve a ticket by id " + ticket.getId());
        if(ticket.getPerformance().getStartDateTime().isBefore(LocalDateTime.now())){
            throw new ForbiddenException();
        }
        if(ticket.getStatus() == TicketStatus.RC || ticket.getStatus() == TicketStatus.IC){
            throw new ServerServiceValidationException("Ticket was cancelled, cannot be reserved");
        }
        List<TicketSeat> ticketSeats = ticketSeatRepository.findTicketSeatsByTicket_Id(ticket.getId());
        ticketSeats.forEach(ticketSeat -> ticketSeat.setStatus(TicketSeatStatus.RESERVED));
        ticketSeatRepository.saveAll(ticketSeats);
        ticket.setStatus(TicketStatus.R);
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket buy(Ticket ticket) {
        LOGGER.info("Buy a ticket by id " + ticket.getId());

        if(ticket.getPerformance().getStartDateTime().isBefore(LocalDateTime.now())){
            throw new ForbiddenException();
        }

        if(ticket.getStatus() == TicketStatus.RC || ticket.getStatus() == TicketStatus.IC){
            throw new ServerServiceValidationException("Ticket is already canceled!");
        }

        //Create invoice first, only then we know that the ticket has been sold
        Long numberOfInvoices = invoiceService.countInvoiceByDateOfIssueYear();

        Invoice invoice = Invoice.builder()
            .dateOfIssue(LocalDateTime.now())
            .ticket(ticket)
            .invoiceNumber(LocalDate.now().getYear() + String.format("%08d", numberOfInvoices+1))
            .build();

        invoice = invoiceService.save(invoice);

        //If successful, ticket is sold
        //Update ticket
        ticket.setStatus(TicketStatus.S);
        List<TicketSeat> ticketSeats = ticketSeatRepository.findTicketSeatsByTicket_Id(ticket.getId());
        ticketSeats.forEach(ticketSeat -> {
            ticketSeat.setStatus(TicketSeatStatus.SOLD);
        });
        ticket = ticketRepository.save(ticket);
        ticketSeatRepository.saveAll(ticketSeats);
        return ticket;
    }

    @Override
    public void delete(Ticket ticket) {
        LOGGER.info("Delete a ticket by id " + ticket.getId());
        //First delete the ticketseats so that foreign key constraint is not violated
        List<TicketSeat> toBeDeleted = ticketSeatRepository.findTicketSeatsByTicket_Id(ticket.getId());
        ticketSeatRepository.deleteAll(toBeDeleted);
        //then delete the ticket from the database
        ticketRepository.delete(ticket);
    }
}
