package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.LocationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SimpleLocationService implements LocationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleLocationService.class);

    private final LocationRepository locationRepository;

    public SimpleLocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Location findById(Long id) {
        LOGGER.info("Find a location by id " + id);
        return locationRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public Page<Location> findByFilter(Boolean descriptionFilter, String description, Boolean streetFilter,
                                       String street, Boolean cityFilter, String city, Boolean countryFilter,
                                       String country, Boolean zipFilter, String zip, Boolean artistFilter,
                                       Long artistId, Boolean eventFilter, Long eventId, Pageable pageable) {
        LOGGER.info("Find locations by filter");
        return locationRepository.findByFilter(descriptionFilter, description, streetFilter,
            street, cityFilter, city, countryFilter, country, zipFilter, zip, artistFilter, artistId, eventFilter, eventId, pageable);
    }
}
