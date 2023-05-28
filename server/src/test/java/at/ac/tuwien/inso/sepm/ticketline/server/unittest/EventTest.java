package at.ac.tuwien.inso.sepm.ticketline.server.unittest;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.EventCategory;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.implementation.SimpleEventService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;


import java.time.LocalDateTime;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class EventTest {

    private static final Long EVENT1_ID = 1L;
    private static final EventCategory EVENT1_CATEGORY = EventCategory.FESTIVAL;
    private static final String EVENT1_TITLE = "Sziget";
    private static final String EVENT1_DESCRIPTION = "Sziget Ungarn";
    private static final LocalDateTime EVENT1_START_DATE = LocalDateTime.of(2018, 8, 8, 0, 0);
    private static final LocalDateTime EVENT1_END_DATE = LocalDateTime.of(2018, 8, 15, 22, 0);
    private static final Integer EVENT1_DURATION = 360;

    private static final Long EVENT2_ID = 2L;
    private static final EventCategory EVENT2_CATEGORY = EventCategory.CINEMA;
    private static final String EVENT2_TITLE = "Marvel's Avengers: Infinity War";
    private static final String EVENT2_DESCRIPTION = "Marvel's newest Avengers movie";
    private static final LocalDateTime EVENT2_START_DATE = LocalDateTime.of(2018, 4, 25, 0, 0);
    private static final LocalDateTime EVENT2_END_DATE = LocalDateTime.of(2018, 8, 25, 0, 0);
    private static final Integer EVENT2_DURATION = 149;

    private static final Long EVENT3_ID = 3L;
    private static final EventCategory EVENT3_CATEGORY = EventCategory.CINEMA;
    private static final String EVENT3_TITLE = "Deadpool 2";
    private static final String EVENT3_DESCRIPTION = "Deadpool 2";
    private static final LocalDateTime EVENT3_START_DATE = LocalDateTime.of(2018, 4, 25, 0, 0);
    private static final LocalDateTime EVENT3_END_DATE = LocalDateTime.of(2018, 8, 25, 0, 0);
    private static final Integer EVENT3_DURATION = 119;

    Event event1;
    Event event2;
    Event event3;

    private List<Event> eventList;
    private Map<Long, Event> eventMap;

    @MockBean
    private EventRepository eventRepository;

    private EventService eventService;

    @Before
    public void setUp(){
        eventService = new SimpleEventService(eventRepository);

        event1 = Event.builder()
            .id(EVENT1_ID)
            .category(EVENT1_CATEGORY)
            .title(EVENT1_TITLE)
            .description(EVENT1_DESCRIPTION)
            .startDate(EVENT1_START_DATE)
            .endDate(EVENT1_END_DATE)
            .duration(EVENT1_DURATION)
            .build();

        event2 = Event.builder()
            .id(EVENT2_ID)
            .category(EVENT2_CATEGORY)
            .title(EVENT2_TITLE)
            .description(EVENT2_DESCRIPTION)
            .startDate(EVENT2_START_DATE)
            .endDate(EVENT2_END_DATE)
            .duration(EVENT2_DURATION)
            .build();

        event3 = Event.builder()
            .id(EVENT3_ID)
            .category(EVENT3_CATEGORY)
            .title(EVENT3_TITLE)
            .description(EVENT3_DESCRIPTION)
            .startDate(EVENT3_START_DATE)
            .endDate(EVENT3_END_DATE)
            .duration(EVENT3_DURATION)
            .build();

        eventList = Arrays.asList(event1, event2, event3);
        eventMap = new HashMap<>();
        eventMap.put(EVENT1_ID, event1);
        eventMap.put(EVENT2_ID, event2);
        eventMap.put(EVENT3_ID, event3);

    }

    @Test
    public void shouldFindAllEvents(){
        Pageable request = PageRequest.of(1, 20);
        BDDMockito
            .given(eventRepository.findAll(request))
            .willReturn(new PageImpl<>(eventList, request, eventList.size()));

        Page<Event> actual = eventService.findAll(request);

        assertEquals(actual.getContent(), eventList);

    }

    @Test
    public void shouldFindOneEvent(){
        BDDMockito.given(eventRepository.findOneById(EVENT1_ID)).willReturn(event1);
        BDDMockito.given(eventRepository.findOneById(EVENT2_ID)).willReturn(event2);

        Event actual1 = eventRepository.findOneById(1L);
        Event actual2 = eventRepository.findOneById(2L);

        assertThat(actual1.getId()).isNotEqualTo(actual2.getId());
    }

    @Test
    public void shouldFindByArtistId(){
        Long someArtistId = 5L;
        Pageable request = PageRequest.of(1, 10);
        List<Event> expected = Arrays.asList(event2, event3);

        BDDMockito
            .given(eventRepository.findByArtistId(someArtistId, request))
            .willReturn(new PageImpl<Event>(expected, request, expected.size()));

        Page<Event> actual = eventService.findByArtistId(someArtistId, request);

        assertThat(actual.getContent()).isEqualTo(expected);

    }

    @Test
    public void shouldFindByCustomCriteria(){

        Pageable request = PageRequest.of(1, 20);
        List<Event> expected = Arrays.asList(event3);

        BDDMockito
            .given(eventRepository.findByCustomCriteria(true, "ead", true, "CINEMA",
                false, "", false, -1, false, -1L,
                false, -1L, request ))
            .willReturn(new PageImpl<>(expected, request, expected.size()));

        Page<Event> actual = eventService.findByCustomCriteria(true, "ead", true, "CINEMA",
                                false, "", false, -1, false, -1L,
                                false, -1L, request);

        assertThat(actual.getContent().size()).isEqualTo(expected.size());
        assertEquals(actual.getContent().get(0), expected.get(0));

    }
}

