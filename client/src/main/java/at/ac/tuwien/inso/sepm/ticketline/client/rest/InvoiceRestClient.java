package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.invoice.InvoiceDTO;

public interface InvoiceRestClient {

    /**
     *
     * @param id id of location
     * @return location dto
     * @throws DataAccessException in case something went wrong
     */
    InvoiceDTO findOneById(Long id) throws DataAccessException;

    /**
     * Fetch the invoice for a specific ticket
     * @param ticket_id id of the ticket
     * @return Invoice of the ticket with the given id
     * @throws DataAccessException in case something went wrong
     */
    InvoiceDTO findByTicketId(Long ticket_id) throws DataAccessException;
}
