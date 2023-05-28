package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.rest.hall.HallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.location.LocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.HallType;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.HallRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;

public class HallEndpointTest extends BaseIntegrationTest {

    private static final String HALL_ENDPOINT = "/hall";
    private static final String SPECIFIC_HALL_PATH = HALL_ENDPOINT + "/performance_id/{id}";
    private static final Long TEST_PERFORMANCE_ID = 1L;


    private static final Long TEST_HALL_ID = 1L;
    private static final String TEST_HALL_DESCRIPTION = "Test Description";
    private static final HallType TEST_HALL_TYPE = HallType.SEAT;
    private static final Integer TEST_HALL_CAPACITY = 100;
    private static final List<Seat> TEST_SEATS = new ArrayList<>();
    private static final List<SeatDTO> TEST_SEAT_DTOs = new ArrayList<>();
    private static final Location TEST_LOCATION = Location.builder()
        .id(1L)
        .houseNumber(11)
        .city("Test City")
        .country("Test Country")
        .street("Test Street")
        .zip("Test Zip")
        .description("Test Description")
        .build();
    private static final LocationDTO TEST_LOCATIONDTO = LocationDTO.builder()
        .id(1L)
        .houseNumber(11)
        .city("Test City")
        .country("Test Country")
        .street("Test Street")
        .zip("Test Zip")
        .description("Test Description")
        .builder();

    @MockBean
    private HallRepository hallRepository;

    @Before
    public void setUp(){
        Seat seat = Seat.builder()
            .id(1L)
            .sector(1)
            .row(1)
            .number(1)
            .build();
        SeatDTO seatDTO = SeatDTO.builder()
            .id(1L)
            .sector(1)
            .row(1)
            .number(1)
            .build();
        TEST_SEATS.add(seat);
        TEST_SEAT_DTOs.add(seatDTO);
    }

    @Test
    public void findHallsByPerformanceIdUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(SPECIFIC_HALL_PATH, TEST_PERFORMANCE_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findHallsByPerformanceIdAsUser() {
        BDDMockito.
            given(hallRepository.findByPerformanceId(TEST_PERFORMANCE_ID)).
            willReturn(Hall.builder()
                .id(TEST_HALL_ID)
                .seats(TEST_SEATS)
                .capacity(TEST_HALL_CAPACITY)
                .description(TEST_HALL_DESCRIPTION)
                .type(TEST_HALL_TYPE)
                .location(TEST_LOCATION)
                .build());

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(SPECIFIC_HALL_PATH, TEST_PERFORMANCE_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(HallDTO.class), is(HallDTO.builder()
            .id(TEST_HALL_ID)
            .type(TEST_HALL_TYPE.name())
            .capacity(TEST_HALL_CAPACITY)
            .description(TEST_HALL_DESCRIPTION)
            .location(TEST_LOCATIONDTO)
            .seats(TEST_SEAT_DTOs)
            .builder()));
    }

}
