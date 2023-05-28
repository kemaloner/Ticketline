package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.PerformanceRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.PerformanceService;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
public class SimplePerformanceService implements PerformanceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimplePerformanceService.class);

    private final PerformanceRestClient performanceRestClient;

    public SimplePerformanceService(PerformanceRestClient performanceRestClient) {
        this.performanceRestClient = performanceRestClient;
    }

    @Override
    public PerformanceDTO findOneById(Long id) throws DataAccessException {
        LOGGER.info("Find a performance by id " + id);
        return performanceRestClient.findOneById(id);
    }

    @Override
    public PaginationWrapper<PerformanceDTO> findByFilter(MultiValueMap multiValueMap, Pageable pageable) throws DataAccessException {
        LOGGER.info("Find performances by filter");
        return performanceRestClient.findByFilter(multiValueMap, pageable);
    }
}
