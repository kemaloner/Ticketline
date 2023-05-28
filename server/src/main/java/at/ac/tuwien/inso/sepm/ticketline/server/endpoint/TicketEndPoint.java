package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;


import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.ReservationNumber;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.ticket.TicketMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.ticketseat.TicketSeatMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.ForbiddenException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.SeatSelectionException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.ServerServiceValidationException;
import at.ac.tuwien.inso.sepm.ticketline.server.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/ticket")
@Api(value = "ticket")
public class TicketEndPoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketEndPoint.class);


    private final TicketService ticketService;
    private final TicketMapper ticketMapper;
    private final ReservationNumberService reservationNumberService;
    private final TicketSeatMapper ticketSeatMapper;
    private final TicketSeatService ticketSeatService;

    public TicketEndPoint(TicketService ticketService, TicketMapper ticketMapper,
                          ReservationNumberService reservationNumberService,
                          TicketSeatMapper ticketSeatMapper,
                          TicketSeatService ticketSeatService) {
        this.ticketService = ticketService;
        this.ticketMapper = ticketMapper;
        this.reservationNumberService = reservationNumberService;
        this.ticketSeatMapper = ticketSeatMapper;
        this.ticketSeatService = ticketSeatService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get list of ticket entries")
    public PaginationWrapper<TicketDTO> findAll(Pageable pageable) {
        LOGGER.info("Loading all tickets");
        Page<Ticket> page = ticketService.findAll(pageable);
        return new PaginationWrapper<>(ticketMapper.ticketToTicketDTO(page.getContent()), page.getTotalPages());
    }

    @RequestMapping(value = "/search/{keyword}", method = RequestMethod.GET)
    @ApiOperation(value = "find by keyword")
    public PaginationWrapper<TicketDTO> findByKeyword(@PathVariable String keyword, Pageable pageable){
        LOGGER.info("Loading tickets by filter");
        Page<Ticket> page = ticketService.findByKeyword(keyword, pageable);
        List<Ticket> ticketList = page.getContent();
        List<TicketDTO> ticketDTOS = ticketMapper.ticketToTicketDTO(ticketList);
        return new PaginationWrapper<>(ticketDTOS, page.getTotalPages());
    }

    @RequestMapping(value = "/newReservationNumber", method = RequestMethod.GET)
    @ApiOperation(value = "requests new reservation number from the database")
    public String requestNewReservationNumber(){
        LOGGER.info("Requesting a new reservation number");
       return reservationNumberService.save(new ReservationNumber());
    }

    @RequestMapping(value = "/cancel/ticket", method = RequestMethod.PUT)
    @ApiOperation(value = "Cancel Ticket")
    public void cancelTicket(@RequestBody TicketDTO ticket){
        LOGGER.info("Canceling a ticket by id " + ticket.getId());
        ticketService.cancelTicket(ticketMapper.ticketDTOToTicket(ticket));
    }

    //Change return type if necessary
    @RequestMapping(value = "/save/all/{performance_id}", method = RequestMethod.POST)
    @ApiOperation(value = "Save a ticket together with its seats")
    public ResponseEntity<?> save(@PathVariable("performance_id") Long performance_id, @RequestBody List<Long> seat_ids){
        LOGGER.info("Saving a ticket");
        ResponseEntity<TicketDTO> toBeReturned;
        try{
            Ticket ticket = ticketService.saveToAnonymous(performance_id, seat_ids);
            toBeReturned = ResponseEntity.ok(ticketMapper.ticketToTicketDTO(ticket));
            return toBeReturned;
        }catch (SeatSelectionException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }catch (ForbiddenException e){
            throw e;
        }
    }

    @RequestMapping(value = "/ticketCount/{ticketId}", method = RequestMethod.GET)
    @ApiOperation(value = "Get Ticket Count")
    public Integer countTicketSeatByTicketId(@PathVariable("ticketId") Long ticketId){
        LOGGER.info("Loading seats number");
        return ticketSeatService.countTicketSeatByTicketId(ticketId);
    }

    @RequestMapping(value = "/update/{ticket_id}", method = RequestMethod.POST)
    @ApiOperation(value = "Update a reservation ticket and its seats")
    public ResponseEntity<?> update(@PathVariable("ticket_id") Long ticket_id, @RequestBody List<Long> seat_ids){
        LOGGER.info("Updating a ticket with id " + ticket_id);
        ResponseEntity<TicketDTO> toBeReturned;
        try{
            Ticket ticket = ticketService.update(ticket_id, seat_ids);
            toBeReturned = ResponseEntity.ok(ticketMapper.ticketToTicketDTO(ticket));
            return toBeReturned;
        }catch (SeatSelectionException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }catch (ServerServiceValidationException | ForbiddenException e){
            throw e;
        }
    }

    @RequestMapping(value = "/reserve", method = RequestMethod.PUT)
    @ApiOperation(value = "Reserve a ticket")
    public TicketDTO reserve(@RequestBody TicketDTO ticketDTO){
        LOGGER.info("Reserving a ticket with id " + ticketDTO.getId());
        try {
            return ticketMapper.ticketToTicketDTO(ticketService.reserve(ticketMapper.ticketDTOToTicket(ticketDTO)));
        }catch (ServerServiceValidationException | ForbiddenException e){
            throw e;
        }
    }

    @RequestMapping(value = "/buy", method = RequestMethod.POST)
    @ApiOperation(value = "Buy a ticket, creating invoice in the process")
    public TicketDTO buy(@RequestBody TicketDTO ticketDTO){
        LOGGER.info("Buying a ticket with id " + ticketDTO.getId());
        try {
            return ticketMapper.ticketToTicketDTO(ticketService.buy(ticketMapper.ticketDTOToTicket(ticketDTO)));
        }catch (ServerServiceValidationException | ForbiddenException e){
            throw e;
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete a temporarily created ticket and its seats")
    public void delete(@RequestBody TicketDTO ticketDTO){
        LOGGER.info("Canceling a ticket with id " + ticketDTO.getId());
        ticketService.delete(ticketMapper.ticketDTOToTicket(ticketDTO));
    }


}
