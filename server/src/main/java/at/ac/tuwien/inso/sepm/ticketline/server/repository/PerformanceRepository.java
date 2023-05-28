package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    /**
     * Find a single performance entry by id.
     *
     * @param id id of the performance
     * @return Optional containing the performance entry
     */
    Optional<Performance> findOneById(Long id);

    /**
     * Find performances by filter
     *
     * @param locationFilter checks if location filter is active
     * @param locationId id of location
     * @param eventFilter checks if event filter is active
     * @param eventId id of event
     * @param priceFilter checks if price filter is active
     * @param price price of performance
     * @param dateTimeFilter checks if date time filter is active
     * @param dateTimeFrom date time of performance lower limit
     * @param dateTimeTo date time of performance upper limit
     * @param artistFilter checks if artist filter is active
     * @param artistId id of artist
     * @param pageable page entity
     * @return List of filtered performances
     */
    @Query(value = "select * from performance p " +
        "where (:locationFilter = false OR (p.id in (select h.id from hall h where h.location_id in " +
        "(select l.id from location l where l.id = :locationId))))" +
        "and (:artistFilter = false or (p.id in (select pa.performance_ID from performance_artist pa where pa.artist_ID = :artistId)))" +
        "and (:eventFilter = false OR (p.event_id in (select e.id from event e where e.id = :eventId)))" +
        "and (:dateTimeFilter = false OR (p.start_date_time >= :dateTimeFrom and p.start_date_time <= :dateTimeTo))" +
        "and (:priceFilter = false OR (p.base_price >= :price-10 and p.base_price <= :price+10))" +
        "and (p.start_date_time >= :currentDateTime)", nativeQuery = true)
    Page<Performance> findByFilter(@Param("locationFilter") boolean locationFilter,
                                   @Param("locationId") Long locationId,
                                   @Param("eventFilter") boolean eventFilter,
                                   @Param("eventId") Long eventId,
                                   @Param("priceFilter") boolean priceFilter,
                                   @Param("price") Double price,
                                   @Param("dateTimeFilter") boolean dateTimeFilter,
                                   @Param("dateTimeFrom") LocalDateTime dateTimeFrom,
                                   @Param("dateTimeTo") LocalDateTime dateTimeTo,
                                   @Param("artistFilter") boolean artistFilter,
                                   @Param("artistId") Long artistId,
                                   @Param("currentDateTime") LocalDateTime currentDateTime,
                                   Pageable pageable);


    /** Update Ticket Seat capacity when ticket sold.
     *
     * @param id of performance, which needs to be updated
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE performance SET left_capacity = (SELECT h.capacity- (SELECT count(*) FROM ticket_seat" +
        "      WHERE status!='FREE' AND performance_id=p.id) FROM performance p LEFT JOIN hall h ON h.id=p.hall_id WHERE p.id = :id) WHERE id = :id", nativeQuery = true)
    void updateSeatCapacity(@Param("id") long id);


}
