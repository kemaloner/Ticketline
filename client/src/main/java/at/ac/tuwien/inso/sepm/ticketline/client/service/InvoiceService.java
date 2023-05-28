package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;


import java.io.FileNotFoundException;

public interface InvoiceService {

    /**
     *
     * @param id id of invoice
     * @return invoice file
     * @throws DataAccessException in case something went wrong
     */
    String findOneById(Long id) throws DataAccessException, FileNotFoundException;

    /**
     * Fetch the invoice for a specific ticket
     * @param ticket_id id of the ticket
     * @return Invoice of the ticket with the given id
     * @throws DataAccessException in case something went wrong
     */
    String findByTicketId(Long ticket_id) throws DataAccessException, FileNotFoundException;


}
