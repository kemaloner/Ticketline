package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PerformanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SimplePerformanceService implements PerformanceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimplePerformanceService.class);

    private final PerformanceRepository performanceRepository;

    public SimplePerformanceService(PerformanceRepository performanceRepository) {
        this.performanceRepository = performanceRepository;
    }

    @Override
    public Performance findById(Long id) {
        LOGGER.info("Find a performance by id " + id);
        return performanceRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public Page<Performance> findByFilter(Boolean locationFilter, Long locationId,
                                          Boolean eventFilter, Long eventId, Boolean priceFilter,
                                          Double price, Boolean dateTimeFilter, LocalDateTime dateTime,
                                          LocalDateTime dateTime2, Boolean artistFilter,
                                          Long artistId, LocalDateTime currentDateTime, Pageable pageable) {
        LOGGER.info("Find performances by filter");
        return performanceRepository.findByFilter(locationFilter, locationId, eventFilter,
            eventId, priceFilter, price, dateTimeFilter, dateTime, dateTime2, artistFilter, artistId, currentDateTime, pageable);
    }
}
