package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.SeatSelectionException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TicketService {

    /**
     * @param request pageRequest
     * @return List of ticket
     */
    Page<Ticket> findAll(Pageable request);

    /**
     * Find a single ticket entry by id.
     * @param id the is of the ticket entry
     * @return the ticket entry
     */
    Ticket findOne(Long id);

    /**
     * @param keyword searched String
     * @param request pageRequest
     * @return Page of costumers
     */
    Page<Ticket> findByKeyword(String keyword, Pageable request);


    /**
     * Cancel a ticket or reservation
     * @param ticket the ticket which should be canceled
     * @return the ticket object which was just cancelled
     */
    Ticket cancelTicket(Ticket ticket);

    /**
     * Saves or updates a ticket in the database
     * @param ticket ticket object to be saved
     * @return saved ticket object with the generated id for further use
     */
    Ticket saveTicket(Ticket ticket);

    /**
     * Saves both the ticket and its seats at the same time
     * @param performance_id id of the performance, for which we create a new ticket
     * @param seat_ids seats we want to reserve
     * @return saved ticket object
     * @throws SeatSelectionException in case at least one of the requested seats were already reserved.
     * if thrown, rolls the transaction back.
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = SeatSelectionException.class)
    Ticket saveToAnonymous(Long performance_id, List<Long> seat_ids) throws SeatSelectionException;

    /**
     * Updates the seats of an already reserved ticket
     * @param ticket_id id of the ticket, for which the reserved seats will be updated
     * @param seat_ids ids of the seats selected by the user
     * @return updated ticket object
     * @throws SeatSelectionException in case at least one of the requested seats were already reserved.
     * if thrown, rolls the transaction back.
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = SeatSelectionException.class)
    Ticket update(Long ticket_id, List<Long> seat_ids) throws SeatSelectionException;

    /**
     * Buys the seats that were previously temporary. Creates an invoice
     * @param ticket Ticket object to be bought
     * @return Ticket object after operation
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    Ticket buy(Ticket ticket);

    /**
     * Reserves the seats which were previously temporary.
     * @param ticket Ticket to be reserved
     * @return Ticket object after operation
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    Ticket reserve(Ticket ticket);

    /**
     * Delete a ticket and its seats from the database.
     * Needed in case user decided to cancel the buying/reserving operation
     * @param ticket ticket to be deleted
     */
    void delete(Ticket ticket);

}
