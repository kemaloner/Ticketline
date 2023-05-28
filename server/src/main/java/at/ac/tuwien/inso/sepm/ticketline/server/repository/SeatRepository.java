package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    /**
     * Tries to find the seat with the given id for a performance in TicketSeat table
     * @param performance_id id of the performance
     * @param seat_id id of the seat
     * @return Seat object if found, null otherwise
     */
    @Query(value = "Select * from Seat s " +
        "where s.id in (Select ts.seat_id from ticket_seat ts " +
                        "where ts.performance_id = :performance_id and ts.seat_id = :seat_id and ts.status != 'FREE')", nativeQuery = true)
    Seat isSeatAvailable(@Param("performance_id") Long performance_id, @Param("seat_id") Long seat_id);

    /**
     * Gets an available seat from a sector
     * @param sector Sector to look for available seats
     * @param performance_id performance in which this sector lies
     * @param seat_id A seat which was already taken, method finds a replacement
     * @param hall_id Id of the hall in which this performance plays
     * @return one available seat from the given sector for a certain performance
     */
    @Query(value = "Select TOP 1 * from Seat s " +
        "where s.sector = :sector and s.id not in (select ts.seat_id from ticket_seat ts " +
        "where ts.performance_id = :performance_id and ts.status != 'FREE') and s.id != :seat_id and s.type = 'STAND' " +
        "and s.hall_id = :hall_id", nativeQuery = true)
    Seat getOneAvailableFromSectorStanding(@Param("sector") Integer sector, @Param("performance_id") Long performance_id,
                                           @Param("seat_id") Long seat_id, @Param("hall_id") Long hall_id);


}
