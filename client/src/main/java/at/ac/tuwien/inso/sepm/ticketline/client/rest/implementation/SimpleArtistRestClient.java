package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.ArtistRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.artist.ArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


@Component
public class SimpleArtistRestClient implements ArtistRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleArtistRestClient.class);
    private static final String ARTIST_URL = "/artist";

    private final RestClient restClient;

    public SimpleArtistRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public ArtistDTO findOneById(Long id) throws DataAccessException {
        try{
            String URI = ARTIST_URL + "/find/byId/" + id;
            LOGGER.debug("Retreiving artist with id {} from {}", id, restClient.getServiceURI(URI));

            ResponseEntity<ArtistDTO> artist =
                restClient.exchange(
                    restClient.getServiceURI(URI),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ArtistDTO>() {
                    });
            return artist.getBody();
        }catch (HttpStatusCodeException e){
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("artist.notfound"), e);
        }catch (RestClientException e){
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public PaginationWrapper<ArtistDTO> findAdvanced(MultiValueMap<String, String> params, Pageable request) throws DataAccessException {
        try{
            String path = ARTIST_URL + "/find/advanced";
            URI url = restClient.getServiceURI(path, request);
            LOGGER.debug("Retreiving matching artists from {}", url);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponents builder = UriComponentsBuilder.fromUri(url.normalize()).queryParams(params).build();
            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<PaginationWrapper<ArtistDTO>> artists =
                restClient.exchange(
                    builder.encode().toUri(),
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<PaginationWrapper<ArtistDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", artists.getStatusCode(), artists.getBody());
            return artists.getBody();
        }catch (HttpStatusCodeException e){
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("artist.notfound"), e);
        }catch (RestClientException e){
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public PaginationWrapper<ArtistDTO> findAll(Pageable request) throws DataAccessException{
        try{
            URI url = restClient.getServiceURI(ARTIST_URL, request);
            LOGGER.debug("Retreiving all artists from {}", url);
            ResponseEntity<PaginationWrapper<ArtistDTO>> artists =
                restClient.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<PaginationWrapper<ArtistDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", artists.getStatusCode(), artists.getBody());
            return artists.getBody();
        }catch (HttpStatusCodeException e){
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("artist.notfound"), e);
        }catch (RestClientException e){
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }
}
