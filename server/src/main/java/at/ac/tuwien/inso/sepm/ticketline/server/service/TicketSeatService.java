package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.rest.ticketseat.SimpleTicketSeatDTOImpl;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketSeat;

import java.util.List;

public interface TicketSeatService {

    List<TicketSeat> findByTicketId(Long ticketId);

    /**
     * Fetches all TicketSeat objects in the performance with the given id
     * @param performance_id id of the performance, for which we request the seats
     * @return A list of TicketSeat objects
     */
    List<SimpleTicketSeatDTOImpl> findByPerformanceId(Long performance_id);

    /**
     * Given a ticket id, counts how many seats have been reserved by it
     * @param ticketId id of the ticket
     * @return Number of reserved seats
     */
    Integer countTicketSeatByTicketId(Long ticketId);
}
