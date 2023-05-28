package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ClientServiceValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticketseat.TicketSeatDTO;

import java.util.List;

public interface TicketSeatService {

    /**
     * Find seats reserved by a ticket by its given id
     * @param ticketId id of the ticket
     * @return List of seats reserved by this ticket
     * @throws DataAccessException in case something went wrong
     * @throws ClientServiceValidationException if ticket does not exist
     */
    List<TicketSeatDTO> findByTicketId(Long ticketId) throws DataAccessException, ClientServiceValidationException;

}
