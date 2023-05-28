package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface PerformanceService {

    /**
     * Find a single performance entry by id.
     *
     * @param id id of the performance
     * @return Optional containing the performance entry
     */
    Performance findById(Long id);

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
     * @param dateTime date time of performance lower limit
     * @param dateTime2 date time of performance upper limit
     * @param artistFilter indicates whether an artistId has been given
     * @param artistId id of the artist
     * @param pageable page entity
     * @return list of filtered performances
     */
    Page<Performance> findByFilter(Boolean locationFilter, Long locationId, Boolean eventFilter,
                                   Long eventId, Boolean priceFilter, Double price, Boolean dateTimeFilter,
                                   LocalDateTime dateTime, LocalDateTime dateTime2,
                                   Boolean artistFilter, Long artistId, LocalDateTime currentDateTime, Pageable pageable);
}
