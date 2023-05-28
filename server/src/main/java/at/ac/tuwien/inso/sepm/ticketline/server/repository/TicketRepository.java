package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    /**
     * Find a single ticket entry by id.
     *
     * @param id the is of the ticket entry
     * @return Optional containing the ticket entry
     */
    Optional<Ticket> findOneById(Long id);

    /**
     *
     * @param reservationNumber  Res Id
     * @param firstname firstname of Customer
     * @param request pageable request
     * @return a list of customer
     */
    Page<Ticket> findByreservationNumberContainingOrCustomer_FirstnameOrCustomer_SurnameContainingAllIgnoreCase(String reservationNumber, String firstname, String surname, Pageable request);


    Page<Ticket> findAllByStatusIsNot(@Param("status") TicketStatus status, Pageable request);


    /** Update Ticket status as Canceled
     *
     * @param status of the ticket
     * @param id of ticket, which needs to be canceled
     */
    @Modifying
    @Transactional
    @Query(value = "update TICKET set status = :status where id = :id", nativeQuery = true)
    void updateTicketStatus(@Param("status") String status, @Param("id") long id);

}
