package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.EventRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.Top10EventDTOImpl;
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
import java.util.List;

@Component
public class SimpleEventRestClient implements EventRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleEventRestClient.class);
    private static final String EVENT_URL = "/event";

    private final RestClient restClient;

    public SimpleEventRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public EventDTO findOneById(Long id) throws DataAccessException {
        try{
            String URI = EVENT_URL + "/find/byId/" + id;
            LOGGER.debug("Retreiving event object with the id {} from {}", id, restClient.getServiceURI(URI));
            ResponseEntity<EventDTO> event =
                restClient.exchange(
                    restClient.getServiceURI(URI),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<EventDTO>() {
                    }
                );
            return event.getBody();
        } catch (HttpStatusCodeException e){
            throw new DataAccessException("Something went wrong when retreiving event");
        } catch (RestClientException e){
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public PaginationWrapper<EventDTO> findAll(Pageable request) throws DataAccessException{
        try{
            URI url = restClient.getServiceURI(EVENT_URL, request);
            LOGGER.debug("Retreiving all events from {}", url);
            ResponseEntity<PaginationWrapper<EventDTO>> events =
                restClient.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<PaginationWrapper<EventDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", events.getStatusCode(), events.getBody());
            return events.getBody();
        } catch (HttpStatusCodeException e){
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("event.notfound"), e);
        }catch (RestClientException e){
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public PaginationWrapper<EventDTO> findByArtistId(Long id, Pageable request) throws DataAccessException {
        try{
            URI url = restClient.getServiceURI(EVENT_URL + "/find/byArtist/" + id, request);
            LOGGER.debug("Retreiving events with the artist from {}", url);
            ResponseEntity<PaginationWrapper<EventDTO>> events =
                restClient.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<PaginationWrapper<EventDTO>>(){}
                );
            LOGGER.debug("Result status was {} with content {}", events.getStatusCode(), events.getBody());
            return events.getBody();
        } catch (HttpStatusCodeException e){
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("event.notfound"), e);
        } catch (RestClientException e){
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public PaginationWrapper<EventDTO> findByCustomCriteria(MultiValueMap<String, String> params, Pageable request) throws DataAccessException {
        try{
            URI url = restClient.getServiceURI(EVENT_URL + "/find/advanced/", request);
            String path = EVENT_URL + "/find/advanced/";
            LOGGER.debug("Retreiving events with given params from {}", url);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponents builder = UriComponentsBuilder.fromUri(url.normalize()).queryParams(params).build();
            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<PaginationWrapper<EventDTO>> events =
                restClient.exchange(
                    builder.encode().toUri(),
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<PaginationWrapper<EventDTO>>() {
                    });

            return events.getBody();
        }catch (HttpStatusCodeException e){
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("event.notfound"), e);
        }catch (RestClientException e){
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public List<Top10EventDTOImpl> findTop10Event(MultiValueMap params) throws DataAccessException {
        try {
            String path = EVENT_URL + "/find/top10";
            URI url = restClient.getServiceURI(path, params);
            LOGGER.debug("Retreiving events with given params from {}", url);
            ResponseEntity<List<Top10EventDTOImpl>> events =
                restClient.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Top10EventDTOImpl>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", events.getStatusCode(), events.getBody());
            return events.getBody();
        } catch (HttpStatusCodeException e){
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("event.notfound"), e);
        }catch (RestClientException e){
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }
}
