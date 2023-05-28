package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.location.LocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

public interface LocationService {

    /**
     *
     * @param id id of location
     * @return location dto
     * @throws DataAccessException in case something went wrong
     */
    LocationDTO findOneById(Long id) throws DataAccessException;

    /**
     *
     * @param multiValueMap filter info
     * @param pageable page info
     * @return list of location dtos
     * @throws DataAccessException in case something went wrong
     */
    PaginationWrapper<LocationDTO> findByFilter(MultiValueMap multiValueMap, Pageable pageable) throws DataAccessException;
}
