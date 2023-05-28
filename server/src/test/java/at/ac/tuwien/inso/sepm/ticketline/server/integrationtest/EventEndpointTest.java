package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.EventCategory;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event.EventMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class EventEndpointTest extends BaseIntegrationTest {

    private static final String EVENT_ENDPOINT = "/event";
    private static final String EVENT_BY_ID = EVENT_ENDPOINT + "/find/byId/";
    private static final String EVENT_BY_ARTIST = EVENT_ENDPOINT + "/find/byArtist/";
    private static final String EVENT_SEARCH_ADVANCED = EVENT_ENDPOINT + "/find/advanced";

    @MockBean
    private EventRepository eventRepository;

    @Autowired
    private EventMapper eventMapper;

    private Event EVENT1, EVENT2;

    private List<Event> mockEventDb;

    private Pageable request = PageRequest.of(0, 20);

    @Before
    public void SetUp(){

        EVENT1 = Event.builder()
            .category(EventCategory.CINEMA)
            .description("Avengers: Infinity War")
            .duration(120)
            .title("MCU")
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now())
            .id(1L)
            .performances(new LinkedList<Performance>())
            .build();

        EVENT2 = Event.builder()
            .category(EventCategory.CONCERT)
            .description("Concert held by Shakira")
            .duration(180)
            .title("Shakira world")
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now())
            .id(2L)
            .performances(new LinkedList<Performance>())
            .build();

        mockEventDb = Arrays.asList(EVENT1, EVENT2);
    }

    @Test
    public void findAllUnauthorized(){

        Pageable request = PageRequest.of(0, 20);
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when()
            .get(getServiceURI(EVENT_ENDPOINT, request))
            .then()
            .extract()
            .response();

        assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void findAll(){

        BDDMockito
            .given(eventRepository.findAll(request))
            .willReturn(new PageImpl<Event>(mockEventDb, request, mockEventDb.size()));

        Response response = RestAssured
            .given()
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when()
            .get(getServiceURI(EVENT_ENDPOINT, request))
            .then()
            .extract()
            .response();

        assertEquals(response.getStatusCode(), HttpStatus.OK.value());
        assertEquals(response.getBody().jsonPath().getList("entities", EventDTO.class), eventMapper.eventToEventDTO(mockEventDb));
    }

    @Test
    public void findByEventId(){

        BDDMockito
            .given(eventRepository.findOneById(1L))
            .willReturn(EVENT1);

        Response response = RestAssured
            .given()
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when()
            .get(getServiceURI(EVENT_BY_ID+"/"+1L, request))
            .then()
            .extract()
            .response();

        assertEquals(response.getStatusCode(), HttpStatus.OK.value());
    }

    @Test
    public void findByArtistId(){

        List<Event> event = Arrays.asList(EVENT1);

        BDDMockito
            .given(eventRepository.findByArtistId(1L, request))
            .willReturn(new PageImpl<Event>(event, request, event.size()));

        Response response = RestAssured
            .given()
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when()
            .get(getServiceURI(EVENT_BY_ARTIST+"/"+1L, request))
            .then()
            .extract()
            .response();

        assertEquals(response.getStatusCode(), HttpStatus.OK.value());
        assertEquals(response.getBody().jsonPath().getList("entities", EventDTO.class), eventMapper.eventToEventDTO(event));
    }

    @Test
    public void findAdvanced(){

        List<Event> event = Arrays.asList(EVENT2);

        BDDMockito
            .given(eventRepository.findByCustomCriteria(true, "Shak", true, EventCategory.CONCERT.toString(),
                false, "", false, -1, false, -1L,
                false, -1L, request))
            .willReturn(new PageImpl<Event>(event, request, event.size()));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("eventTitle", "Shak");
        params.add("eventCategory", "CONCERT");

        String url = getServiceURI(EVENT_SEARCH_ADVANCED, params, request);

        Response response = RestAssured
            .given()
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when()
            .get(url)
            .then()
            .extract()
            .response();

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(eventMapper.eventToEventDTO(event), response.getBody().jsonPath().getList("entities", EventDTO.class));


    }



}
