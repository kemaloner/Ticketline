package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.HallRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.HallService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SimpleHallService implements HallService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleHallService.class);

    private HallRepository hallRepository;

    public SimpleHallService(HallRepository hallRepository){
        this.hallRepository = hallRepository;
    }

    @Override
    public Hall findByPerformanceId(Long id) {
        LOGGER.info("Find a hall by performance id " + id);
        return hallRepository.findByPerformanceId(id);
    }
}
