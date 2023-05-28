package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    /**
     * Find a single location entry by id.
     *
     * @param id id of the location
     * @return Optional containing the location entry
     */
    Optional<Location> findById(Long id);

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
    @Query(value = "select * from location l where " +
        "(:artistFilter = false or l.id in (select h.id from hall h where h.id in (" +
        "select p.hall_id from performance p where p.id in (" +
        "select pa.performance_ID from performance_artist pa where pa.artist_ID = :artistId))))" +
        "and (:descriptionFilter = false or upper(description) like  upper(concat('%', :description, '%')))" +
        "and (:streetFilter = false or upper(street) like upper(concat('%', :street, '%')))" +
        "and (:cityFilter = false or upper(city) like upper(concat('%', :city, '%')))" +
        "and (:countryFilter = false or upper(country) like upper(concat('%', :country, '%')))" +
        "and (:zipFilter = false or upper(zip) like upper(concat('%', :zip, '%')))" +
        "and (:eventFilter = false or l.id in(select h.location_id from hall h where h.id in (" +
        "select p.hall_id from performance p where p.event_Id = :eventId)))", nativeQuery = true)
    Page<Location> findByFilter(@Param("descriptionFilter") boolean descriptionFilter,
                                @Param("description") String description,
                                @Param("streetFilter") boolean streetFilter,
                                @Param("street") String street,
                                @Param("cityFilter") boolean cityFilter,
                                @Param("city") String city,
                                @Param("countryFilter") boolean countryFilter,
                                @Param("country") String country,
                                @Param("zipFilter") boolean zipFilter,
                                @Param("zip") String zip,
                                @Param("artistFilter") boolean artistFilter,
                                @Param("artistId") Long artistId,
                                @Param("eventFilter") boolean eventFilter,
                                @Param("eventId") Long eventId,
                                Pageable pageable);
}
