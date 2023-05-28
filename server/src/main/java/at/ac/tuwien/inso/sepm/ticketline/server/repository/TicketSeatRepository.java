package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.rest.ticketseat.SimpleTicketSeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TicketSeatRepository extends JpaRepository<TicketSeat, Long> {

    @Query(value = "select * from ticket_seat ts where ts.ticket_id in " +
        "(select t.id from Ticket t where t.performance_id = :performance_id and t.status != 'C') and ts.status != 'FREE'", nativeQuery = true)
    List<TicketSeat> findByPerformanceId(@Param("performance_id") Long performance_id);


    /** Find seat list which related with this ticket
     *
     * @param ticket_id id of the ticket
     */
    List<TicketSeat> findTicketSeatsByTicket_Id(Long ticket_id);


    /** update Ticket seat status as FREE
     *
     * @param id of the ticket
     */
    @Modifying
    @Transactional
    @Query(value = "update ticket_seat set status = 'FREE' where id = :id", nativeQuery = true)
    void updateTicketSeatStatus(@Param("id") long id);

    /**
     * Given a ticket id, counts how many seats have been reserved by it
     * @param ticketId id of the ticket
     * @return Number of reserved seats
     */
    @Query(value = "SELECT count(t.TICKET_ID) FROM TICKET_SEAT t WHERE t.TICKET_ID = :ticketId", nativeQuery = true)
    Integer countTicketSeatByTicketId(@Param("ticketId") Long ticketId);

    @Query
    List<SimpleTicketSeatDTO> findTicketSeatsByPerformanceId(@Param("performance_id") Long performance_id);

}
