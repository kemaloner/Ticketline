package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.Top10EventDTOImpl;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.EventCategory;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event.EventMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/event")
@Api(value = "event")
public class EventEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventEndpoint.class);


    private final EventService eventService;
    private final EventMapper eventMapper;

    public EventEndpoint(EventService eventService, EventMapper eventMapper){
        this.eventService = eventService;
        this.eventMapper = eventMapper;
    }

    @RequestMapping(value = "/find/byId/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Find an event entry by id")
    public EventDTO findOneById(@PathVariable("id") Long id){
        LOGGER.info("Finding an event by id " + id);
        return eventMapper.eventToEventDTO(eventService.findOneById(id));
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Find all event entries")
    public PaginationWrapper<EventDTO> findAll(Pageable request)
    {
        LOGGER.info("Loading all events");
        Page events = eventService.findAll(request);
        return new PaginationWrapper<EventDTO>(eventMapper.eventToEventDTO(events.getContent()), events.getTotalPages());
    }

    @RequestMapping(value = "/find/byArtist/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Find event entries in which the given artist plays")
    public PaginationWrapper<EventDTO> findByArtistId(@PathVariable("id") Long id, Pageable request){
        LOGGER.info("Loading events that contain a curtain artist with id " + id);
        Page<Event> events = eventService.findByArtistId(id, request);
        return new PaginationWrapper<EventDTO>(eventMapper.eventToEventDTO(events.getContent()), events.getTotalPages());
    }

    @RequestMapping(value = "/find/advanced", method = RequestMethod.GET)
    @ApiOperation(value = "Find event entries with the provided search criteria")
    public PaginationWrapper<EventDTO> findAdvanced(@RequestParam HashMap<String, String> params, Pageable request){
        LOGGER.info("Loading events by filter");
        boolean isTitle, isCategory, isDescription, isDuration, isArtistId, isLocationId;
        String title, description;
        Integer duration;
        Long artistId, locationId;
        isTitle = false;
        isCategory = false;
        isDescription = false;
        isDuration = false;
        isArtistId = false;
        isLocationId = false;
        title = "";
        description = "";
        duration = -1;
        artistId = -1L;
        locationId = -1L;
        String category = "";

        if(params.containsKey("eventTitle")){
            isTitle = true;
            title = params.get("eventTitle");
        }

        if(params.containsKey("eventCategory")){
            isCategory = true;
            category = EventCategory.fromString(params.get("eventCategory"));
        }

        if(params.containsKey("eventDescription")){
            isDescription = true;
            description = params.get("eventDescription");
        }

        if(params.containsKey("eventDuration")){
            try {
                isDuration = true;
                duration = Integer.parseInt(params.get("eventDuration"));
            }catch (NumberFormatException e){
                isDuration = false;
                duration = -1;
            }
        }

        if(params.containsKey("eventArtistId")){
            isArtistId = true;
            artistId = Long.parseLong(params.get("eventArtistId"));
        }

        if(params.containsKey("eventLocationId")){
            isLocationId = true;
            locationId = Long.parseLong(params.get("eventLocationId"));
        }


        Page<Event> events = eventService.findByCustomCriteria(isTitle, title, isCategory, category, isDescription,
                                                                description, isDuration, duration, isArtistId, artistId,
                                                                isLocationId, locationId, request);
        return new PaginationWrapper<EventDTO>(eventMapper.eventToEventDTO(events.getContent()), events.getTotalPages());
    }

    @RequestMapping(value = "/find/top10", method = RequestMethod.GET)
    @ApiOperation(value = "Find top 10 event entries with the provided search criteria")
    List<Top10EventDTOImpl> findTop10Event(@RequestParam HashMap<String, String> params){
        LOGGER.info("Loading top 10 events");
        LocalDateTime startDate = LocalDateTime.parse(params.get("startDate"));
        //LocalDateTime endDate = startDate.plusMonths(1);
        boolean isCategory = false;
        String category = null;

        if (params.containsKey("category")){
            isCategory = true;
            category = params.get("category");
        }
        return eventService.findTop10Event(startDate, isCategory, category);
    }
}
