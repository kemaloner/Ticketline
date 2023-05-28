package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.SeatSelectionException;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TicketRestClient {

    /**
     * Find all ticket entries.
     *
     * @return list of ticket entries
     * @param  request pageable request
     * @throws DataAccessException in case something went wrong
     */
    PaginationWrapper<TicketDTO> findAll(Pageable request) throws DataAccessException;

    /**
     * Find ticket entries by keyword
     *
     * @param keyword of ticket entry
     * @param request pageable request
     * @return page of ticket entries
     * @throws DataAccessException in case something went wrong
     */
    PaginationWrapper<TicketDTO> findByKeyword(String keyword, Pageable request) throws DataAccessException;

    /**
     * Fetches a new unique reservation number from the database
     * @return String representing reservation number
     */
    String requestNewReservationNumber() throws DataAccessException;


    /**
     * Cancel a ticket or reservation
     * @param ticket the ticket which should be canceled
     */
    void cancelTicket(TicketDTO ticket) throws DataAccessException;

    /**
     * Save a ticket together with its seats
     * @param performanceDTO performance to which a ticket will be saved
     * @param seat_ids id's of the seats that need to be saved
     * @return a list of saved ticket_seats upon success
     * @throws DataAccessException in case something went wrong
     */
    TicketDTO save(PerformanceDTO performanceDTO, List<Long> seat_ids) throws DataAccessException, SeatSelectionException;

    Integer countTicketSeatByTicketId(Long ticketId) throws DataAccessException;

    /**
     * Update a given ticket and its seats. To be used when reselecting seats for a reservation
     * @param ticketDTO ticket to be updated
     * @param seat_ids list of seats, which a user selected, after some ticket has been reserved
     * @return updated ticket
     * @throws DataAccessException In case something went wrong
     * @throws SeatSelectionException In case some selected seats have already been taken.
     */
    TicketDTO save(TicketDTO ticketDTO, List<Long> seat_ids) throws DataAccessException, SeatSelectionException;

    /**
     * Reserves the seats that were previously temporary. Creates an invoice
     * @param ticketDTO Ticket object to be reserved
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
