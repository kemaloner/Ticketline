package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ArtistService {

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
    Page<Artist> findAdvanced(boolean isFirstnameGiven, String firstname, boolean isSurnameGiven, String surname,
                              boolean isEventIdGiven, Long eventId, Pageable request);

    /**
     * Finds every artist entry in the given page
     * @param request Pageable request
     * @return A PaginationWrapper object containing results and pagination relevant information
     */
    Page<Artist> findAll(Pageable request);
}
