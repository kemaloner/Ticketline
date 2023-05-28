package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.rest.artist.ArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.artist.ArtistMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ArtistRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpHeaders;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ArtistEndpointTest extends BaseIntegrationTest {

    private static final String ARTIST_ENDPOINT = "/artist";
    private static final String ARTIST_BY_ID = ARTIST_ENDPOINT + "/find/byId/";
    private static final String ARTIST_SEARCH_ADVANCED = ARTIST_ENDPOINT + "/find/advanced";

    @MockBean
    private ArtistRepository artistRepository;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    private ArtistMapper artistMapper;

    private Artist artist1, artist2;

    List<Artist> mockArtistDb;

    @Before
    public void setUp(){
        artist1 = Artist.builder()
            .id(1L)
            .firstname("Katy")
            .lastname("Perry")
            .performances(new LinkedList<Performance>())
            .build();

        artist2 = Artist.builder()
            .id(2L)
            .firstname("Ben")
            .lastname("Affleck")
            .performances(new LinkedList<Performance>())
            .build();

        mockArtistDb = Arrays.asList(artist1, artist2);

    }

    @Test
    public void findAllUnauthorized(){
        Response response = RestAssured.given()
            .contentType(ContentType.JSON)
            .when()
            .get(ARTIST_ENDPOINT)
            .then()
            .extract()
            .response();

        assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void findAllAuthorized(){
        Pageable request = PageRequest.of(0, 2);

        BDDMockito
            .given(artistRepository.findAll(request))
            .willReturn(new PageImpl<Artist>(mockArtistDb, request, mockArtistDb.size()));

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when()
            .get(getServiceURI(ARTIST_ENDPOINT, request))
            .then()
            .extract()
            .response();

        assertEquals(response.getStatusCode(), HttpStatus.OK.value());
        assertEquals(response.getBody().jsonPath().getList("entities", ArtistDTO.class), artistMapper.artistToArtistDTO(mockArtistDb));

    }

    @Test
    public void findAdvanced(){
        Pageable request = PageRequest.of(0, 2);

        List<Artist> artist = Arrays.asList(artist1);
        BDDMockito
            .given(artistRepository.findAdvanced(false, "", false, "", false, -1L, request))
            .willReturn(new PageImpl<Artist>(artist, request, artist.size()));

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when()
            .get(getServiceURI(ARTIST_SEARCH_ADVANCED, request))
            .then()
            .extract()
            .response();

        assertEquals(response.getStatusCode(), HttpStatus.OK.value());
        assertEquals(response.getBody().jsonPath().getList("entities", ArtistDTO.class), artistMapper.artistToArtistDTO(artist));
    }


}
