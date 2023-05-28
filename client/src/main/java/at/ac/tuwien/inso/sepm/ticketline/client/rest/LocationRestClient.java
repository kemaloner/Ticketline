package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.location.LocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import org.springframework.util.MultiValueMap;
import org.springframework.data.domain.Pageable;

public interface LocationRestClient {

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
