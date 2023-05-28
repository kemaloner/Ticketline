package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Invoice;

public interface InvoiceService {

    /**
     * Find a single invoice entry by id.
     *
     * @param id the is of the invoice entry
     * @return Optional containing the invoice entry
     */
    Invoice findOneById(Long id);

    /**
     *  Fetch an invoice for a ticket
     * @param ticket_id id of the ticket, for which we want the invoice
     * @return Invoice created for a specific ticket
     */
    Invoice findByTicketId(Long ticket_id);

    /**
     * Fetches the number of invoices created this year
     * @return Number of invoices
     */
    Long countInvoiceByDateOfIssueYear();

    /**
     * Creates a new invoice entry in the database
     * @param invoice invoice object to be saved
     * @return saved invoice
     */
    Invoice save(Invoice invoice);
}
