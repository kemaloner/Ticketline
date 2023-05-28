package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.performance.PerformanceMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpHeaders;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class PerformanceEndpointTest extends BaseIntegrationTest {

    private static final String PERFORMANCE_ENDPOINT = "/performance";
    private static final String PERFORMANCE_BY_ID = PERFORMANCE_ENDPOINT + "/findById/";
    private static final String PERFORMANCE_BY_FILTER = PERFORMANCE_ENDPOINT + "/findByFilter";

    @MockBean
    private PerformanceRepository performanceRepository;

    @Autowired
    private PerformanceMapper performanceMapper;

    private Performance performance1, performance2;
    private List<Performance> performances;
    private Pageable request;
    private MultiValueMap<String, String> params;

    @Before
    public void setUp(){

        performance1 = Performance.builder()
            .artists(new LinkedList<Artist>())
            .basePrice(150d)
            .endDateTime(LocalDateTime.now().plusDays(1L))
            .startDateTime(LocalDateTime.now())
            .event(null)
            .hall(null)
            .leftCapacity(50)
            .id(1L)
            .build();

        performance2 = Performance.builder()
            .artists(new LinkedList<Artist>())
            .basePrice(150d)
            .endDateTime(LocalDateTime.now().plusDays(2L))
            .startDateTime(LocalDateTime.now().plusDays(1L))
            .event(null)
            .hall(null)
            .leftCapacity(50)
            .id(2L)
            .build();

        performances = Arrays.asList(performance1, performance2);

        request = PageRequest.of(0,20);
    }

    @Test
    public void findOneByIdUnauthorizedAsAnonymous(){

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when()
            .get(PERFORMANCE_BY_ID + 1L)
            .then()
            .extract()
            .response();

        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());
    }

    @Test
    public void findOneByIdAuthorizedAsUser(){

        BDDMockito
            .given(performanceRepository.findById(1L))
            .willReturn(Optional.of(performance1));

        Response response = RestAssured
            .given()
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .contentType(ContentType.JSON)
            .when()
            .get(PERFORMANCE_BY_ID + performance1.getId())
            .then()
            .extract()
            .response();

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());


    }

    @Test
    public void findOneByIdAuthorizedAsAdmin(){

        BDDMockito
            .given(performanceRepository.findById(1L))
            .willReturn(Optional.of(performance1));

        Response response = RestAssured
            .given()
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .contentType(ContentType.JSON)
            .when()
            .get(PERFORMANCE_BY_ID + performance1.getId())
            .then()
            .extract()
            .response();

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void findNonExistingAuthorizedAsUser(){

        BDDMockito
            .given(performanceRepository.findOneById(1L))
            .willReturn(Optional.empty());

        Response response = RestAssured
            .given()
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .contentType(ContentType.JSON)
            .when()
            .get(PERFORMANCE_BY_ID + 1L)
            .then()
            .extract()
            .response();

        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }

    @Test
    public void findNonExistingAuthorizedAsAdmin(){

        BDDMockito
            .given(performanceRepository.findOneById(1L))
            .willReturn(Optional.of(performance1));

        Response response = RestAssured
            .given()
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .contentType(ContentType.JSON)
            .when()
            .get(PERFORMANCE_BY_ID + 1L)
            .then()
            .extract()
            .response();

        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }






}
