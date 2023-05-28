package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.PerformanceRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
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
public class SimplePerformanceRestClient implements PerformanceRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimplePerformanceRestClient.class);
    private static final String PERFORMANCE_URL = "/performance";


    private final RestClient restClient;

    public SimplePerformanceRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public PerformanceDTO findOneById(Long id) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving found by ID performance from {}", restClient.getServiceURI(PERFORMANCE_URL)+"/findById/"+id);
            ResponseEntity<PerformanceDTO> performance =
                restClient.exchange(
                    restClient.getServiceURI(PERFORMANCE_URL+"/findById/"+id),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<PerformanceDTO>() {
                    });
            LOGGER.debug("Result status was {} with content {}", performance.getStatusCode(), performance.getBody());
            return performance.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("performance.notfound"), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public PaginationWrapper<PerformanceDTO> findByFilter(MultiValueMap multiValueMap, Pageable pageable) throws DataAccessException {
        try {
            String path = PERFORMANCE_URL + "/findByFilter";
            URI url = restClient.getServiceURI(path, pageable);
            UriComponents builder = UriComponentsBuilder.fromUri(url.normalize()).queryParams(multiValueMap).build();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
            LOGGER.debug("Retrieving filtered performances from {}", builder);
            ResponseEntity<PaginationWrapper<PerformanceDTO>> performances =
                restClient.exchange(
                    builder.encode().toUri(),
                    HttpMethod.GET,
                    httpEntity,
                    new ParameterizedTypeReference<PaginationWrapper<PerformanceDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", performances.getStatusCode(), performances.getBody());
            return performances.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("performance.notfound"), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }
}
