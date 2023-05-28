package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    /**
     * Finds an artist entry by the given id
     * @param id id of the artist
     * @return An artist entry
     */
    Artist findOneById(Long id);


    /**
     * Finds artist entries that match the given criteria
     * @param isFirstnameGiven indicates that firstname parameter is provided
     * @param firstname firstname input
     * @param isSurnameGiven indicates that surname parameter is provided
     * @param surname surname input
     * @param isEventIdGiven indicates whether event id parameter is provided
     * @param eventId id of the event
     * @param request Pageable request
     * @return Page containing the matching artist entries
     */
    @Query(value = "select * from Artist a where (:isFirstnameGiven = FALSE OR UPPER(a.firstname) like UPPER(concat('%', :firstname, '%'))) " +
        "AND (:isSurnameGiven = FALSE OR UPPER(a.surname) like upper(concat('%', :surname, '%') ) ) " +
        "AND (:isEventIdGiven = FALSE OR a.id in (select  pa.artist_id from PERFORMANCE_ARTIST pa " +
                                                "where pa.performance_id in (select p.id from PERFORMANCE p " +
                                                                            "where p.event_id in (select e.id from EVENT e " +
                                                                                                "where e.id = :eventId))))", nativeQuery = true)
    Page<Artist> findAdvanced(@Param("isFirstnameGiven") boolean isFirstnameGiven, @Param("firstname") String firstname,
                              @Param("isSurnameGiven") boolean isSurnameGiven, @Param("surname") String surname,
                              @Param("isEventIdGiven") boolean isEventIdGiven, @Param("eventId") Long eventId,
                              Pageable request);

}
