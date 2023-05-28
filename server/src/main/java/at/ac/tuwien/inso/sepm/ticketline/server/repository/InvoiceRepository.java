package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    /**
     * Find a single invoice entry by id.
     *
     * @param id the id of the invoice entry
     * @return Optional containing the invoice entry
     */
    Optional<Invoice> findOneByInvoiceNumber(Long id);


    /**
     *  Fetch an invoice for a ticket
     * @param ticket_id id of the ticket, for which we want the invoice
     * @return Invoice created for a specific ticket
     */
    @Query(value = "Select * from Invoice i where i.ticket_id = :ticket_id", nativeQuery = true)
    Optional<Invoice> findByTicketId(@Param("ticket_id") Long ticket_id);

    /**
     * Fetch the amount of invoices already been created this year
     * @return The amount of invoices created this year
     */
    @Query(value = "SELECT COUNT(*) FROM INVOICE WHERE YEAR(DATE_OF_ISSUE) =  YEAR(CURRENT_TIMESTAMP)", nativeQuery = true)
    Long countInvoiceByDateOfIssueYear();
}
