package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.Top10EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Finds an event entry by the given id
     * @param id id of the event we want to find
     * @return an Event object
     */
    Event findOneById(Long id);


    /**
     * Finds Event entries, in which the artist with the given id performs
     * @param id id of the artist
     * @param request Pageable object containing pagination info
     * @return Page of event entries
     */
    @Query(value = "select * from event e " +
                    "where e.id in (Select p.event_id from performance p " +
                                    "where p.id in (Select ap.performance_id from performance_artist ap " +
                                                    "where ap.artist_id = :artist_id))", nativeQuery = true)
    Page<Event> findByArtistId(@Param("artist_id")Long id, Pageable request);

    /**
     * Finds event entries depending on the given search criteria
     * @param isTitle Indicates that event title has been inputted
     * @param title Title string for the event
     * @param isCategory Indicates that event category has been inputtet
     * @param category Category of the event
     * @param isDescription Indicates that a description string has been inputted
     * @param description Description of the event
     * @param isDuration Indicates whether a duration has been given
     * @param duration Duration of the event
     * @param isArtistId indicates whether an artist id has been given
     * @param artistId id of the artist
     * @param isLocationId indicates whether a location has been given
     * @param locationId id of the location
     * @param request Pageable object containing pagination info
     * @return A list of event entries conforming to the search criteria
     */
    @Query(value = "select * from Event e " +
                    "where (:isTitle = false or (upper(e.title) like upper(concat('%', :title, '%') ) ))" +
                    " AND (:isCategory = false or (e.category = :category))" +
                    " AND (:isDescription = false or (upper(e.description ) like upper(concat('%', :description, '%') ) ))" +
                    " AND (:isDuration = false or (e.duration >= :duration - 30 and e.duration <= :duration + 30) )" +
                    " AND (:isArtistId = false or (e.id in (Select p.event_id from Performance p " +
                                                            "where p.id in (Select ap.performance_id from performance_artist ap" +
                                                                            " where ap.artist_id = :artistId))))" +
                    " AND (:isLocationId = false or (e.id in (Select p.event_id from Performance p " +
                                                                " where p.hall_id in (Select h.id from hall h " +
                                                                                        "where h.location_id = :locationId))))", nativeQuery = true)
    Page<Event> findByCustomCriteria(@Param("isTitle") boolean isTitle,
                                     @Param("title") String title,
                                     @Param("isCategory") boolean isCategory,
                                     @Param("category") String category,
                                     @Param("isDescription") boolean isDescription,
                                     @Param("description") String description,
                                     @Param("isDuration") boolean isDuration,
                                     @Param("duration") Integer duration,
                                     @Param("isArtistId") boolean isArtistId,
                                     @Param("artistId") Long artistId,
                                     @Param("isLocationId") boolean isLocationId,
                                     @Param("locationId") Long locationId,
                                     Pageable request);


    /**
     * Finds top 10 events according to chosen criteria
     * @param startDateTime start time of the event
     * @param isCategory indicates whether a category was given
     * @param category category of the event
     * @return A list of Top 10 events
     */
    @Query
    List<Top10EventDTO> findTop10Event(@Param("startDateTime")LocalDateTime startDateTime,
                                       @Param("endDateTime")LocalDateTime endDateTime,
                                       @Param("isCategory") boolean isCategory,
                                       @Param("category") String category);

}
