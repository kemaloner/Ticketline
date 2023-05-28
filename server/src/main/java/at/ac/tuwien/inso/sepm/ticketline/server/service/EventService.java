package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.Top10EventDTOImpl;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    /**
     * Finds an event entry by the given id
     * @param id id of the event we want to find
     * @return an Event object
     */
    Event findOneById(Long id);

    /**
     * Finds all Event entries
     * @param request Pageable object containing pagination info
     * @return Page object containing every Event entry
     */
    Page<Event> findAll(Pageable request);

    /**
     * Finds Event entries, in which the artist with the given id performs
     * @param id id of the artist
     * @param request Pageable object containing pagination info
     * @return Page of event entries
     */
    Page<Event> findByArtistId(Long id, Pageable request);

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
    Page<Event> findByCustomCriteria(boolean isTitle, String title, boolean isCategory, String category,
                                     boolean isDescription, String description, boolean isDuration, Integer duration,
                                     boolean isArtistId, Long artistId, boolean isLocationId, Long locationId,
                                     Pageable request);

    /**
     * Finds top 10 events according to chosen criteria
     * @param startDateTime start time of the event
     * @param isCategory indicates whether a category was given
     * @param category category of the event
     * @return A list of Top 10 events
     */
    List<Top10EventDTOImpl> findTop10Event(LocalDateTime startDateTime, boolean isCategory, String category);
}
