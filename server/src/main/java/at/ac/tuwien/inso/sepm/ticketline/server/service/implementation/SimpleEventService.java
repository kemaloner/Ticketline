package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.Top10EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.Top10EventDTOImpl;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SimpleEventService implements EventService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleEventService.class);

    private final EventRepository eventRepository;

    public SimpleEventService(EventRepository eventRepository){
        this.eventRepository = eventRepository;
    }

    @Override
    public Event findOneById(Long id) {
        LOGGER.info("Find an event by id " + id);
        return eventRepository.findOneById(id);
    }

    @Override
    public Page<Event> findAll(Pageable request) {
        LOGGER.info("Find all events");
        return eventRepository.findAll(request);
    }

    @Override
    public Page<Event> findByArtistId(Long id, Pageable request) {
        LOGGER.info("Find an event by artist id " + id);
        return eventRepository.findByArtistId(id, request);
    }

    @Override
    public Page<Event> findByCustomCriteria(boolean isTitle, String title, boolean isCategory, String category,
                                            boolean isDescription, String description,
                                            boolean isDuration, Integer duration, boolean isArtistId,
                                            Long artistId, boolean isLocationId, Long locationId, Pageable request) {
        LOGGER.info("Find events by filter");
        return eventRepository.findByCustomCriteria(isTitle, title, isCategory, category, isDescription, description,
                                                    isDuration, duration, isArtistId, artistId, isLocationId, locationId,
                                                    request);
    }

    @Override
    public List<Top10EventDTOImpl> findTop10Event(LocalDateTime startDateTime, boolean isCategory, String category) {
        LOGGER.info("Find top 10 events");
        List<Top10EventDTO> results = eventRepository.findTop10Event(startDateTime, startDateTime.plusMonths(1), isCategory, category);
        List<Top10EventDTOImpl> top10Events = new ArrayList<>();
        results.forEach(s -> top10Events.add(new Top10EventDTOImpl(s)));
        return top10Events;
    }
}
