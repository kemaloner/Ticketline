package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.TicketRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.TicketService;
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SimpleTicketService implements TicketService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleTicketService.class);

    private final TicketRestClient ticketRestClient;

    public SimpleTicketService(TicketRestClient ticketRestClient) {
        this.ticketRestClient = ticketRestClient;
    }

    @Override
    public PaginationWrapper<TicketDTO> findAll(Pageable request) throws DataAccessException {
        LOGGER.info("Find all tickets");
        return ticketRestClient.findAll(request);
    }

    @Override
    public PaginationWrapper<TicketDTO> findByKeyword(String keyword, Pageable request) throws DataAccessException {
        LOGGER.info("Find tickets by filter");
        return ticketRestClient.findByKeyword(keyword,request);
    }

    @Override
    public Double calculateTicketSubTotal(PerformanceDTO performanceDTO, List<SeatDTO> seatDTOS) {
        LOGGER.info("Calculating ticket sub-total");
        Double subTotal = 0d;
        Double basePrice = performanceDTO.getBasePrice();

        for(SeatDTO seatDTO : seatDTOS){
            subTotal += basePrice*seatDTO.getMultiplier();
        }

        return subTotal;
    }

    @Override
    public String requestNewReservationNumber() throws DataAccessException{
        LOGGER.info("Requesting new reservation number");
        return ticketRestClient.requestNewReservationNumber();

    }

    @Override
    public void cancelTicket(TicketDTO ticket) throws DataAccessException {
        LOGGER.info("Calcel a ticket by id " + ticket.getId());
        if(ticket.getStatus() != TicketStatus.IC || ticket.getStatus() != TicketStatus.RC){
            ticketRestClient.cancelTicket(ticket);
        }
    }

    @Override
    public Integer countTicketSeatByTicketId(Long ticketId) throws DataAccessException {
        LOGGER.info("Count of ticket seat of ticket id " + ticketId);
        return ticketRestClient.countTicketSeatByTicketId(ticketId);
    }

    @Override
    public TicketDTO reserve(TicketDTO ticketDTO) throws DataAccessException{
        LOGGER.info("Reserve a ticket by id " + ticketDTO.getId());
        return ticketRestClient.reserve(ticketDTO);
    }

    @Override
    public TicketDTO buy(TicketDTO ticketDTO)throws DataAccessException {
        LOGGER.info("Buy a ticket by id " + ticketDTO.getId());
        return ticketRestClient.buy(ticketDTO);
    }

    @Override
    public void delete(TicketDTO ticketDTO) throws DataAccessException {
        LOGGER.info("Delete a ticket by id " + ticketDTO.getId());
        ticketRestClient.delete(ticketDTO);
    }
}
