package at.ac.tuwien.inso.sepm.ticketline.server.mapper;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event.EventMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.performance.PerformanceMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class EventMapperTest {

    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper")
    public static class EventMapperTestContextConfiguration {
    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private EventMapper eventMapper;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private PerformanceMapper performanceMapper;

    private static final Long EVENT_ID = 1L;
    private static final EventCategory EVENT_CATEGORY = EventCategory.CINEMA;
    private static final String EVENT_TITLE = "Title";
    private static final String EVENT_DESCRIPTION = "Description";
    private static final Integer EVENT_DURATION = 120;
    private static final LocalDateTime EVENT_STARTDATE = LocalDateTime.of(2018, 1, 1,1,0,0,0);
    private static final LocalDateTime EVENT_ENDDATE =LocalDateTime.of(2018, 2, 1,3,0,0,0);

    private static final List<Performance> PERFORMANCES = new ArrayList<>();

    @Before
    public void setUp(){

        Artist artist = Artist.builder()
            .id(1L)
            .firstname("Firstname")
            .lastname("Surname")
            .build();

        Hall hall = Hall.builder()
            .id(1L)
            .capacity(100)
            .description("Description")
            .type(HallType.STAND)
            .build();

        Event event = Event.builder()
            .id(1L)
            .category(EventCategory.CINEMA)
            .description("Description")
            .endDate(LocalDateTime.of(2018, 1, 1,1,0,0,0))
            .startDate(LocalDateTime.of(2018, 3, 1,1,0,0,0))
            .title("Title")
            .build();

        List<Artist> artists = new ArrayList<>();
        artists.add(artist);

        Performance performance = Performance.builder()
            .id(1L)
            .basePrice(50.0)
            .startDateTime(LocalDateTime.of(2018, 1, 1,1,0,0,0))
            .endDateTime(LocalDateTime.of(2018, 1, 1,3,0,0,0))
            .artists(artists)
            .hall(hall)
            .event(event)
            .build();

        PERFORMANCES.add(performance);
    }

    @Test
    public void shouldMapEventDTOToEvent(){
        List<PerformanceDTO> performanceDTOS = performanceMapper.performanceToPerformanceDTO(PERFORMANCES);

        EventDTO eventDTO = EventDTO.builder()
            .id(EVENT_ID)
            .category(EVENT_CATEGORY.name())
            .description(EVENT_DESCRIPTION)
            .endDate(EVENT_ENDDATE)
            .startDate(EVENT_STARTDATE)
            .title(EVENT_TITLE)
            .duration(EVENT_DURATION)
            .performances(performanceDTOS)
            .builder();

        Event event = eventMapper.eventDTOToEvent(eventDTO);
        assertThat(event).isNotNull();
        assertThat(event.getId()).isEqualTo(EVENT_ID);
        assertThat(event.getCategory()).isEqualTo(EVENT_CATEGORY);
        assertThat(event.getDescription()).isEqualTo(EVENT_DESCRIPTION);
        assertThat(event.getEndDate()).isEqualTo(EVENT_ENDDATE);
        assertThat(event.getStartDate()).isEqualTo(EVENT_STARTDATE);
        assertThat(event.getTitle()).isEqualTo(EVENT_TITLE);
        assertThat(event.getDuration()).isEqualTo(EVENT_DURATION);
        assertThat(event.getPerformances()).isEqualTo(PERFORMANCES);
    }

    @Test
    public void shouldMapEventToEventDTO(){
        Event event = Event.builder()
            .id(EVENT_ID)
            .category(EVENT_CATEGORY)
            .description(EVENT_DESCRIPTION)
            .endDate(EVENT_ENDDATE)
            .startDate(EVENT_STARTDATE)
            .title(EVENT_TITLE)
            .duration(EVENT_DURATION)
            .performances(PERFORMANCES)
            .build();

        EventDTO eventDTO = eventMapper.eventToEventDTO(event);

        List<PerformanceDTO> performanceDTOS = performanceMapper.performanceToPerformanceDTO(PERFORMANCES);
        assertThat(eventDTO).isNotNull();
        assertThat(eventDTO.getId()).isEqualTo(EVENT_ID);
        assertThat(eventDTO.getCategory()).isEqualTo(EVENT_CATEGORY.name());
        assertThat(eventDTO.getDescription()).isEqualTo(EVENT_DESCRIPTION);
        assertThat(eventDTO.getEndDate()).isEqualTo(EVENT_ENDDATE);
        assertThat(eventDTO.getStartDate()).isEqualTo(EVENT_STARTDATE);
        assertThat(eventDTO.getTitle()).isEqualTo(EVENT_TITLE);
        assertThat(eventDTO.getDuration()).isEqualTo(EVENT_DURATION);
        assertThat(eventDTO.getPerformances()).isEqualTo(performanceDTOS);
    }
}
