package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.location.LocationMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.LocationRepository;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class LocationEndpointTest extends BaseIntegrationTest {

    private static final String LOCATION_ENDPOINT = "/location";
    private static final String LOCATION_BY_ID = LOCATION_ENDPOINT + "/findById/";
    private static final String LOCATION_BY_FILTER = LOCATION_ENDPOINT + "/findByFilter/";

    @MockBean
    private LocationRepository locationRepository;
    @Autowired
    private LocationMapper locationMapper;
    private Location location1, location2;
    private List<Location> locations;
    private MultiValueMap<String, String> params;
    private Pageable request;

    @Before
    public void setUp(){

        location1 = Location.builder()
            .city("Vienna")
            .country("Austria")
            .description("Wiener Stadthalle")
            .halls(new LinkedList<Hall>())
            .houseNumber(1)
            .street("Roland Rainer Platz")
            .zip("1150")
            .id(1L)
            .build();

        location2 = Location.builder()
            .city("Vienna")
            .country("Austria")
            .description("Schloss Schönbrunn")
            .halls(new LinkedList<Hall>())
            .houseNumber(1)
            .street("Schönbrunner Allee")
            .zip("1130")
            .id(1L)
            .build();

        locations = Arrays.asList(location1, location2);

        params = new LinkedMultiValueMap<>();
        params.add("city", "Vie");
        params.add("description", "Stadthalle");

        request = PageRequest.of(0, 20);
    }

    @Test
    public void findLocationByIdUnauthorizedAsAnonymous(){

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when()
            .get(LOCATION_BY_ID + 1L)
            .then()
            .extract()
            .response();

        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());
    }

    @Test
    public void findLocationByFilterUnauthorizedAsAnonymous(){

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when()
            .get(getServiceURI(LOCATION_BY_FILTER, params))
            .then()
            .extract()
            .response();

        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());

    }

    @Test
    public void findNonExistingLocationByIdAuthorizedAsUser(){

        BDDMockito
            .given(locationRepository.findById(3L))
            .willReturn(Optional.empty());

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when()
            .get(LOCATION_BY_ID + 3L)
            .then()
            .extract()
            .response();

        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }

    @Test
    public void findLocationByIdAuthorizedAsAdmin(){

        BDDMockito
            .given(locationRepository.findById(1L))
            .willReturn(Optional.of(location1));

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when()
            .get(LOCATION_BY_ID + 1L)
            .then()
            .extract()
            .response();

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void findLocationByIdAuthorizedAsUser(){
        BDDMockito
            .given(locationRepository.findById(1L))
            .willReturn(Optional.of(location1));

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when()
            .get(LOCATION_BY_ID + 1L)
            .then()
            .extract()
            .response();

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void findLocationByFilterAuthorizedAsUser(){

        BDDMockito.given(
            locationRepository.findByFilter(false, "", false, "", false
                , "", false, "", false, "", false, null,
                false, null, request))
            .willReturn(new PageImpl<>(locations, request, locations.size()));

        params = new LinkedMultiValueMap<>();

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .get(getServiceURI(LOCATION_BY_FILTER, params))
            .then()
            .extract()
            .response();

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());

    }

    @Test
    public void findLocationByFilterAuthorizedAsAdmin(){
        BDDMockito.given(
            locationRepository.findByFilter(false, "", false, "", false
                , "", false, "", false, "", false, null,
                false, null, request))
            .willReturn(new PageImpl<>(locations, request, locations.size()));

        params = new LinkedMultiValueMap<>();

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .get(getServiceURI(LOCATION_BY_FILTER, params))
            .then()
            .extract()
            .response();

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());

    }

}
