package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.LocationRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.location.LocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.data.domain.Pageable;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class SimpleLocationRestClient implements LocationRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleLocationRestClient.class);
    private static final String LOCATION_URL = "/location";


    private final RestClient restClient;

    public SimpleLocationRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public LocationDTO findOneById(Long id) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving found by ID location from {}", restClient.getServiceURI(LOCATION_URL)+"/findById/"+id);
            ResponseEntity<LocationDTO> locations =
                restClient.exchange(
                    restClient.getServiceURI(LOCATION_URL+"/findById/"+id),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<LocationDTO>() {
                    });
            LOGGER.debug("Result status was {} with content {}", locations.getStatusCode(), locations.getBody());
            return locations.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("location.notfound"), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public PaginationWrapper<LocationDTO> findByFilter(MultiValueMap multiValueMap, Pageable pageable) throws DataAccessException {
        try {
            String path = LOCATION_URL+"/findByFilter";
            URI url = restClient.getServiceURI(path, pageable);
            UriComponents builder = UriComponentsBuilder.fromUri(url.normalize()).queryParams(multiValueMap).build();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
            LOGGER.debug("Retrieving filtered locations from {}", builder);
            ResponseEntity<PaginationWrapper<LocationDTO>> locations =
                restClient.exchange(
                    builder.encode().toUri(),
                    HttpMethod.GET,
                    httpEntity,
                    new ParameterizedTypeReference<PaginationWrapper<LocationDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", locations.getStatusCode(), locations.getBody());
            return locations.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("location.notfound"), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }
}
