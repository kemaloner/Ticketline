package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketSeatStatus;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticketseat.SimpleTicketSeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticketseat.SimpleTicketSeatDTOImpl;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.SeatType;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketSeatRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.core.Is.is;

public class HallPlanEndpointTest extends BaseIntegrationTest {

    private static final String HALL_PLAN_ENDPOINT = "/hall_plan";
    private static final String SPECIFIC_HALL_PLAN_PATH = HALL_PLAN_ENDPOINT + "/performance_id/{id}";
    private static final String SPECIFIC_HALL_PLAN_IMAGE_PATH = HALL_PLAN_ENDPOINT + "/image/{id}/{lang}";
    private static final Long TEST_PERFORMANCE_ID = 1L;
    private static final Long TEST_HALL_PLAN_ID = 1L;
    private static final String TEST_LANGUAGE_INVALID = "de_invalid";
    private static final String TEST_LANGUAGE = "en";

    private static final Long TEST_TICKET_ID = 1L;
    private static final String TEST_TICKET_SEAT_STATUS_ID = TicketSeatStatus.RESERVED.name();
    private static final Long TEST_TICKET_SEAT_ID = 1L;
    private static final Integer TEST_TICKET_SEAT_SECTOR = 1;
    private static final Integer TEST_TICKET_SEAT_ROW = 1;
    private static final Integer TEST_TICKET_SEAT_NUMBER = 1;
    private static final String TEST_SEAT_TYPE = SeatType.SEAT.name();
    private static final Double TEST_TICKET_SEAT_MULTIPLIER = 1d;
    private static final Integer TEST_TICKET_SEAT_X = 1;
    private static final Integer TEST_TICKET_SEAT_Y = 1;
    private static final Integer TEST_TICKET_SEAT_ANGLE = 1;

    @MockBean
    private TicketSeatRepository ticketSeatRepository;


    @Test
    public void findTicketSeatByPerformanceIdUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(SPECIFIC_HALL_PLAN_PATH, TEST_PERFORMANCE_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findTicketSeatByPerformanceIdAsUser() {
        BDDMockito.
            given(ticketSeatRepository.findTicketSeatsByPerformanceId(TEST_PERFORMANCE_ID)).
            willReturn(Collections.singletonList(
                new SimpleTicketSeatDTO() {
                    @Override
                    public Long getId() {
                        return TEST_TICKET_SEAT_ID;
                    }

                    @Override
                    public Integer getSector() {
                        return TEST_TICKET_SEAT_SECTOR;
                    }

                    @Override
                    public Integer getRow() {
                        return TEST_TICKET_SEAT_ROW;
                    }

                    @Override
                    public Integer getNumber() {
                        return TEST_TICKET_SEAT_NUMBER;
                    }

                    @Override
                    public String getType() {
                        return TEST_SEAT_TYPE;
                    }

                    @Override
                    public Double getMultiplier() {
                        return TEST_TICKET_SEAT_MULTIPLIER;
                    }

                    @Override
                    public Integer getX() {
                        return TEST_TICKET_SEAT_X;
                    }

                    @Override
                    public Integer getY() {
                        return TEST_TICKET_SEAT_Y;
                    }

                    @Override
                    public Integer getAngle() {
                        return TEST_TICKET_SEAT_ANGLE;
                    }

                    @Override
                    public String getStatus() {
                        return TEST_TICKET_SEAT_STATUS_ID;
                    }

                    @Override
                    public Long getTicketId() {
                        return TEST_TICKET_ID;
                    }
                }
            ));

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(SPECIFIC_HALL_PLAN_PATH, TEST_PERFORMANCE_ID)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(Arrays.asList(response.as(SimpleTicketSeatDTOImpl[].class)),
            is(Collections.singletonList(
                new SimpleTicketSeatDTOImpl(TEST_TICKET_SEAT_ID, TEST_TICKET_SEAT_SECTOR, TEST_TICKET_SEAT_ROW, TEST_TICKET_SEAT_NUMBER,
                    TEST_SEAT_TYPE, TEST_TICKET_SEAT_MULTIPLIER, TEST_TICKET_SEAT_X, TEST_TICKET_SEAT_Y,
                    TEST_TICKET_SEAT_ANGLE, TEST_TICKET_SEAT_STATUS_ID, TEST_TICKET_ID))));
    }

    @Test
    public void findHallBackgroundImageUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(SPECIFIC_HALL_PLAN_IMAGE_PATH, TEST_HALL_PLAN_ID, TEST_LANGUAGE)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findHallInvalidBackgroundImageAsUser() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(SPECIFIC_HALL_PLAN_IMAGE_PATH, TEST_HALL_PLAN_ID, TEST_LANGUAGE_INVALID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void findHallValidBackgroundImageAsUser() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(SPECIFIC_HALL_PLAN_IMAGE_PATH, TEST_HALL_PLAN_ID, TEST_LANGUAGE)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }
}
