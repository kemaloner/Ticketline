package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.LocationRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.LocationService;
import at.ac.tuwien.inso.sepm.ticketline.rest.location.LocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
public class SimpleLocationService implements LocationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleLocationService.class);

    private final LocationRestClient locationRestClient;

    public SimpleLocationService(LocationRestClient locationRestClient) {
        this.locationRestClient = locationRestClient;
    }

    @Override
    public LocationDTO findOneById(Long id) throws DataAccessException {
        LOGGER.info("Find a location by id " + id);
        return locationRestClient.findOneById(id);
    }

    @Override
    public PaginationWrapper<LocationDTO> findByFilter(MultiValueMap multiValueMap, Pageable pageable) throws DataAccessException {
        LOGGER.info("Find locations by filter");
        return locationRestClient.findByFilter(multiValueMap, pageable);
    }
}
