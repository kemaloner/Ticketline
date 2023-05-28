package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.HallPlanRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.hall.HallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticketseat.SimpleTicketSeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticketseat.SimpleTicketSeatDTOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Component
public class SimpleHallPlanRestClient implements HallPlanRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleHallPlanRestClient.class);
    private static final String HALL_URL = "/hall";
    private static final String HALL_PLAN_URL = "/hall_plan";
    private static final String HALL_PLAN_IMAGE_URL = HALL_PLAN_URL + "/image/%d/%s";
    private static final String URL_TICKET_SEATS_BY_PERFORMANCE_ID = HALL_PLAN_URL + "/performance_id/%d";
    private static final String URL_HALL_BY_PERFORMANCE_ID = HALL_URL + "/performance_id/%d";
    private RestClient restClient;

    public SimpleHallPlanRestClient(RestClient restClient){
        this.restClient = restClient;
    }

    @Override
    public HallDTO findHallByPerformanceId(Long id) throws DataAccessException {
        URI uri = restClient.getServiceURI(String.format(URL_HALL_BY_PERFORMANCE_ID,id));
        LOGGER.debug("Retrieving hall from {}", uri);
        try {
            ResponseEntity<HallDTO> exchange =
                restClient.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<HallDTO>() {});
            LOGGER.debug("Result status was {} with content {}", exchange.getStatusCode(), exchange.getBody());
            return exchange.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Something went wrong when fetching performances hall");
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public List<SimpleTicketSeatDTO> findTicketSeatsByPerformanceId(Long id) throws DataAccessException {
        URI uri = restClient.getServiceURI(String.format(URL_TICKET_SEATS_BY_PERFORMANCE_ID,id));
        LOGGER.debug("Retrieving seats from {}", uri);
        try{
            ResponseEntity<List<SimpleTicketSeatDTOImpl>> exchange =
                restClient.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<SimpleTicketSeatDTOImpl>>(){});
            return new ArrayList<>(exchange.getBody());
        }catch (HttpStatusCodeException e){
            throw new DataAccessException("Something went wrong when fetching seats for this performance");
        }catch (RestClientException e){
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public byte[] getBackgroundImage(Long hall_id, String lang) throws DataAccessException {
        URI uri = restClient.getServiceURI(String.format(HALL_PLAN_IMAGE_URL,hall_id,lang));
        LOGGER.debug("Retrieving background image of hall from {}", uri);
        try{
            ResponseEntity<byte[]> exchange =
                restClient.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<byte[]>(){});
            return exchange.getBody();
        }catch (HttpStatusCodeException e){
            throw new DataAccessException("Something went wrong when fetching background image of hall");
        }catch (RestClientException e){
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }
}
