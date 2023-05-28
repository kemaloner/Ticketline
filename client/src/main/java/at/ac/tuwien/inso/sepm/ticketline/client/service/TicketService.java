package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TicketService {


    /**
     * Find all Tickets entries.
     *
     * @return list of ticket entries
     * @throws DataAccessException in case something went wrong
     * @param request Pageable request
     */
    PaginationWrapper<TicketDTO> findAll(Pageable request) throws DataAccessException;

    /**
     * Find Ticket entries by Keyword
     *
     * @param keyword of ticket entry
     * @param request Pageable request
     * @return list of ticket entries
     * @throws DataAccessException in case something went wrong
     */
    PaginationWrapper<TicketDTO> findByKeyword(String keyword,Pageable request) throws DataAccessException;

    /**
     * Calculates the subtotal of a ticket
     * @param performanceDTO Performance, of which we need the baseprice
     * @param seatDTOS selected seats, of which we will get their multiplier
     * @return subtotal price of the ticket
     */
    Double calculateTicketSubTotal(PerformanceDTO performanceDTO, List<SeatDTO> seatDTOS);

    /**
     * Fetches a new unique reservation number from the database
     * @return String representing reservation number
     * @throws DataAccessException in case something went wrong
     */
    String requestNewReservationNumber() throws DataAccessException;

    /**
     * Cancel a ticket or reservation
     * @param ticket the ticket which should be canceled
     * @throws DataAccessException in case something went wrong
     */
    void cancelTicket(TicketDTO ticket) throws DataAccessException;

    /**
     * Given a ticket id, counts how many seats have been reserved by it
     * @param ticketId id of the ticket
     * @return Number of reserved seats
     * @throws DataAccessException in case something went wrong
     */
    Integer countTicketSeatByTicketId(Long ticketId) throws DataAccessException;

    /**
     * Buys the seats that were previously temporary. Creates an invoice
     * @param ticketDTO Ticket object to be bought
     * @return Ticket object after operation
     * @throws DataAccessException in case something went wrong
     */
    TicketDTO reserve(TicketDTO ticketDTO) throws DataAccessException;

    /**
     * Reserves the seats that were previously temporary. Creates an invoice
     * @param ticketDTO Ticket object to be reserved
     * @return Ticket object after operation
     * @throws DataAccessException in case something went wrong
     */
    TicketDTO buy(TicketDTO ticketDTO) throws DataAccessException;

    /**
     * Delete a ticket and its seats from the database.
     * Needed in case user decided to cancel the buying/reserving operation
     * @param ticketDTO ticket to be deleted
     * @throws DataAccessException in case something went wrong
     */
    void delete(TicketDTO ticketDTO) throws DataAccessException;
}
