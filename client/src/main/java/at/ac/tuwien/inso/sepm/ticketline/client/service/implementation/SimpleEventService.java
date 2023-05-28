package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.EventRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.Top10EventDTOImpl;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Service
public class SimpleEventService implements EventService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleEventService.class);

    private final EventRestClient eventRestClient;

    public SimpleEventService(EventRestClient eventRestClient){
        this.eventRestClient = eventRestClient;
    }

    @Override
    public EventDTO findOneById(Long id) throws DataAccessException {
        LOGGER.info("Find an event by id " + id);
        return eventRestClient.findOneById(id);
    }

    @Override
    public PaginationWrapper<EventDTO> findAll(Pageable request) throws DataAccessException {
        LOGGER.info("Find all events");
        return eventRestClient.findAll(request);
    }

    @Override
    public PaginationWrapper<EventDTO> findByArtistId(Long id, Pageable request) throws DataAccessException {
        LOGGER.info("Find an event by artist id " + id);
        return eventRestClient.findByArtistId(id, request);
    }

    @Override
    public PaginationWrapper<EventDTO> findByCustomCriteria(MultiValueMap<String, String> params, Pageable request) throws DataAccessException {
        LOGGER.info("Find events by filter");
        return eventRestClient.findByCustomCriteria(params, request);
    }

    @Override
    public List<Top10EventDTOImpl> findTop10Event(MultiValueMap params) throws DataAccessException {
        LOGGER.info("Find top 10 events");
        return eventRestClient.findTop10Event(params);
    }
}
