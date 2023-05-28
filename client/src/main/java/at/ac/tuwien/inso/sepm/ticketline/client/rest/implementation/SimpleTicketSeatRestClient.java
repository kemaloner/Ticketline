package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.TicketSeatRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticketseat.TicketSeatDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Component
public class SimpleTicketSeatRestClient implements TicketSeatRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleTicketSeatRestClient.class);

    private static final String TICKET_SEAT_URL = "/ticketSeat";

    private final RestClient restClient;

    public SimpleTicketSeatRestClient(RestClient restClient){
        this.restClient = restClient;
    }

    @Override
    public List<TicketSeatDTO> findByTicketId(Long ticketId) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving all unread news from {}", restClient.getServiceURI(TICKET_SEAT_URL));
            ResponseEntity<List<TicketSeatDTO>> ticketSeat =
                restClient.exchange(
                    restClient.getServiceURI(TICKET_SEAT_URL +"/findByTicketId/"+ticketId),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<TicketSeatDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", ticketSeat.getStatusCode(), ticketSeat.getBody());
            return ticketSeat.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("ticket.notfound"), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }
}
