package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

public interface PerformanceService {

    /**
     *
     * @param id id of performance
     * @return  performance dto
     * @throws DataAccessException in case something went wrong
     */
    PerformanceDTO findOneById(Long id) throws DataAccessException;

    /**
     *
     * @param multiValueMap filter information map
     * @param pageable page info
     * @return list of performance dtos
     * @throws DataAccessException in case something went wrong
     */
    PaginationWrapper<PerformanceDTO> findByFilter(MultiValueMap multiValueMap, Pageable pageable) throws DataAccessException;
}
