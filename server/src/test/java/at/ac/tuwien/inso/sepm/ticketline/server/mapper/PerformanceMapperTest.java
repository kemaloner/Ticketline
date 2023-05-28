package at.ac.tuwien.inso.sepm.ticketline.server.mapper;

import at.ac.tuwien.inso.sepm.ticketline.rest.artist.ArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.hall.HallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.artist.ArtistMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event.EventMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.hall.HallMapper;
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
public class PerformanceMapperTest {

    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper")
    public static class PerformanceMapperTestContextConfiguration {
    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private PerformanceMapper performanceMapper;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private ArtistMapper artistMapper;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private HallMapper hallMapper;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private EventMapper eventMapper;

    private static final Long PERFORMANCE_ID = 1L;
    private static final LocalDateTime PERFORMANCE_STARTDATE = LocalDateTime.of(2018, 1, 1,1,0,0,0);
    private static final LocalDateTime PERFORMANCE_ENDDATE =LocalDateTime.of(2018, 1, 1,3,0,0,0);
    private static final Double PERFORMANCE_BASEPRICE = 50.0;
    private static final Integer PERFORMANCE_LEFTCAPACITY = 75;

    private static final Event EVENT = Event.builder()
        .id(1L)
        .category(EventCategory.CINEMA)
        .description("Description")
        .endDate(LocalDateTime.of(2018, 1, 1,1,0,0,0))
        .startDate(LocalDateTime.of(2018, 3, 1,1,0,0,0))
        .title("Title")
        .build();

    private static final Hall HALL = Hall.builder()
        .id(1L)
        .capacity(100)
        .description("Description")
        .type(HallType.STAND)
        .build();

    private static final List<Artist> ARTISTS = new ArrayList<>();

    @Before
    public void setUp(){
        Artist artist = Artist.builder()
            .id(1L)
            .firstname("Firstname")
            .lastname("Surname")
            .build();
        ARTISTS.add(artist);
    }

    @Test
    public void shouldMapPerformanceDTOToPerformance(){
        List<ArtistDTO> artistDTOS = artistMapper.artistToArtistDTO(ARTISTS);
        HallDTO hallDTO = hallMapper.hallToHallDTO(HALL);
        EventDTO eventDTO = eventMapper.eventToEventDTO(EVENT);
        PerformanceDTO performanceDTO = PerformanceDTO.builder()
            .id(PERFORMANCE_ID)
            .basePrice(PERFORMANCE_BASEPRICE)
            .startDateTime(PERFORMANCE_STARTDATE)
            .endDateTime(PERFORMANCE_ENDDATE)
            .leftCapacity(PERFORMANCE_LEFTCAPACITY)
            .artists(artistDTOS)
            .hall(hallDTO)
            .event(eventDTO)
            .builder();

        Performance performance = performanceMapper.performanceDTOToPerformance(performanceDTO);
        assertThat(performance).isNotNull();
        assertThat(performance.getId()).isEqualTo(PERFORMANCE_ID);
        assertThat(performance.getBasePrice()).isEqualTo(PERFORMANCE_BASEPRICE);
        assertThat(performance.getStartDateTime()).isEqualTo(PERFORMANCE_STARTDATE);
        assertThat(performance.getEndDateTime()).isEqualTo(PERFORMANCE_ENDDATE);
        assertThat(performance.getEvent()).isEqualTo(EVENT);
        assertThat(performance.getLeftCapacity()).isEqualTo(PERFORMANCE_LEFTCAPACITY);
        assertThat(performance.getHall()).isEqualTo(HALL);
        assertThat(performance.getArtists()).isEqualTo(ARTISTS);
    }

    @Test
    public void shouldMapPerformanceToPerformanceDTO(){
        Performance performance = Performance.builder()
            .id(PERFORMANCE_ID)
            .basePrice(PERFORMANCE_BASEPRICE)
            .startDateTime(PERFORMANCE_STARTDATE)
            .endDateTime(PERFORMANCE_ENDDATE)
            .artists(ARTISTS)
            .leftCapacity(PERFORMANCE_LEFTCAPACITY)
            .hall(HALL)
            .event(EVENT)
            .build();

        PerformanceDTO performanceDTO = performanceMapper.performanceToPerformanceDTO(performance);

        List<ArtistDTO> artistDTOS = artistMapper.artistToArtistDTO(ARTISTS);
        HallDTO hallDTO = hallMapper.hallToHallDTO(HALL);
        EventDTO eventDTO = eventMapper.eventToEventDTO(EVENT);
        assertThat(performanceDTO).isNotNull();
        assertThat(performanceDTO.getId()).isEqualTo(PERFORMANCE_ID);
        assertThat(performanceDTO.getBasePrice()).isEqualTo(PERFORMANCE_BASEPRICE);
        assertThat(performanceDTO.getStartDateTime()).isEqualTo(PERFORMANCE_STARTDATE);
        assertThat(performanceDTO.getEndDateTime()).isEqualTo(PERFORMANCE_ENDDATE);
        assertThat(performanceDTO.getLeftCapacity()).isEqualTo(PERFORMANCE_LEFTCAPACITY);
        assertThat(performanceDTO.getEvent()).isEqualTo(eventDTO);
        assertThat(performanceDTO.getHall()).isEqualTo(hallDTO);
        assertThat(performanceDTO.getArtists()).isEqualTo(artistDTOS);
    }
}
