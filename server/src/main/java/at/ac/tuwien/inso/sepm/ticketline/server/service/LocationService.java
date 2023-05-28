package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LocationService {

    /**
     * Find a single location entry by id.
     *
     * @param id of the searched location
     * @return the location with given id
     */
    Location findById(Long id);

    /**
     * Find locations by filter
     *
     * @param descriptionFilter checks if description filter is active
     * @param description description of location
     * @param streetFilter checks if street filter is active
     * @param street street of location
     * @param cityFilter checks if city filter is active
     * @param city city of location
     * @param countryFilter checks if country filter is active
     * @param country country of location
     * @param zipFilter checks if zip filter is active
     * @param zip zip adress of location
     * @param artistFilter checks if artist filter is active
     * @param artistId id of artist
     * @param eventFilter checks if event filter is active
     * @param eventId id of event
     * @param pageable page entity
     * @return list of filtered locations
     */
    Page<Location> findByFilter(Boolean descriptionFilter, String description, Boolean streetFilter,
                                String street, Boolean cityFilter, String city, Boolean countryFilter,
                                String country, Boolean zipFilter, String zip, Boolean artistFilter,
                                Long artistId, Boolean eventFilter, Long eventId, Pageable pageable);
}
