package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.SeatSelectionException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.TicketRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.net.URI;
import java.util.List;

@Component
public class SimpleTicketRestClient implements TicketRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleTicketRestClient.class);
    private static final String TICKET_URL = "/ticket";
    private static final String TICKET_SEARCH_URL = TICKET_URL + "/search/%s";
    private static final String SAVE_ALL = TICKET_URL + "/save/all";
    private static final String SAVE_TICKET = TICKET_URL + "/save/ticket";
    private static final String CANCEL_TICKET = TICKET_URL + "/cancel/ticket";
    private static final String TICKET_COUNT = TICKET_URL + "/ticketCount/%d";
    private static final String UPDATE_TICKET = TICKET_URL + "/update/%d";
    private static final String RESERVE_TICKET = TICKET_URL + "/reserve";
    private static final String BUY_TICKET = TICKET_URL + "/buy";
    private static final String DELETE_TICKET = TICKET_URL + "/delete";

    private final RestClient restClient;

    public SimpleTicketRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public PaginationWrapper<TicketDTO> findAll(Pageable request) throws DataAccessException {
        try {
            URI url = restClient.getServiceURI(TICKET_URL, request);
            LOGGER.debug("Retrieving all tickets from {}", url);
            ResponseEntity<PaginationWrapper<TicketDTO>> tickets =
                restClient.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<PaginationWrapper<TicketDTO>>() {});
            LOGGER.debug("Result status was {} with content {}", tickets.getStatusCode(), tickets.getBody());
            return tickets.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("ticket.notfound"), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public PaginationWrapper<TicketDTO> findByKeyword(String keyword, Pageable request) throws DataAccessException {
        try {
            URI url = restClient.getServiceURI(String.format(TICKET_SEARCH_URL,keyword), request);
            LOGGER.debug("Retrieving all tickets from {}", url);
            ResponseEntity<PaginationWrapper<TicketDTO>> tickets =
                restClient.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<PaginationWrapper<TicketDTO>>(){});
            LOGGER.debug("Result status was {} with content {}", tickets.getStatusCode(), tickets.getBody());
            return tickets.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("ticket.notfound"), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public String requestNewReservationNumber() throws DataAccessException {

        try {
            URI url = restClient.getServiceURI(TICKET_URL + "/newReservationNumber");
            LOGGER.debug("Retreiving new unique reservation number from {}", url);

            ResponseEntity<String> reservationNumber =
                restClient.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<String>() {
                    }
                );

            LOGGER.debug("Result status was {} with content {}", reservationNumber.getStatusCode(), reservationNumber.getBody());
            return reservationNumber.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("ticket.notfound"), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public void cancelTicket(TicketDTO ticket) throws DataAccessException {

        try {
            URI url = restClient.getServiceURI(CANCEL_TICKET);
            HttpEntity<TicketDTO> entity = new HttpEntity<>(ticket);
            LOGGER.info("Cancel {} at {}", ticket.getReservationNumber(), url);

            ResponseEntity<TicketDTO> response =
                restClient.exchange(
                    url,
                    HttpMethod.PUT,
                    entity,
                    new ParameterizedTypeReference<TicketDTO>() {});
            LOGGER.debug("Cancel {} status {}", ticket.getReservationNumber(), response.getStatusCode());
        } catch (HttpStatusCodeException e) {
            if(e.getStatusCode() == HttpStatus.NOT_ACCEPTABLE){
                throw new DataAccessException(BundleManager.getExceptionBundle().getString("ticket.cannotcancel"),e);
            }else {
                throw new DataAccessException(BundleManager.getExceptionBundle().getString("ticket.notfound"), e);
            }
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public TicketDTO save(PerformanceDTO performanceDTO, List<Long> seat_ids) throws DataAccessException, SeatSelectionException {
        try{
            URI url = restClient.getServiceURI(SAVE_ALL + "/" + performanceDTO.getId());
            LOGGER.info("Saving ticket and its seats to {}", url);
            HttpEntity<List<Long>> entity = new HttpEntity<>(seat_ids);

            ResponseEntity<TicketDTO> exchange =
                restClient.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<TicketDTO>() {
                    }
                );
            LOGGER.debug("Ticket saving with status {}", exchange.getStatusCode());
            return exchange.getBody();
        }catch (HttpStatusCodeException e){
            if(e.getStatusCode() == HttpStatus.CONFLICT){
                throw new SeatSelectionException(e.getResponseBodyAsString());
            }else if(e.getStatusCode() == HttpStatus.NOT_FOUND){
                throw new DataAccessException(BundleManager.getExceptionBundle().getString("performance.notfound"));
            }else{
                throw new DataAccessException(BundleManager.getExceptionBundle().getString("performance.started"));
            }
        }catch (RestClientException e){
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public TicketDTO save(TicketDTO ticketDTO, List<Long> seat_ids) throws DataAccessException, SeatSelectionException{
        try{
            URI url = restClient.getServiceURI(String.format(UPDATE_TICKET, ticketDTO.getId()));
            LOGGER.info("Update ticket and its seats to {}", url);
            HttpEntity<List<Long>> entity = new HttpEntity<>(seat_ids);

            ResponseEntity<TicketDTO> exchange =
                restClient.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<TicketDTO>() {
                    }
                );
            LOGGER.debug("Ticket saving with status {}", exchange.getStatusCode());
            return exchange.getBody();
        }catch (HttpStatusCodeException e){
            if(e.getStatusCode() == HttpStatus.CONFLICT){
                throw new SeatSelectionException(e.getResponseBodyAsString());
            }else{
                throw new DataAccessException(BundleManager.getExceptionBundle().getString("performance.started"));
            }

        }catch (RestClientException e){
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public Integer countTicketSeatByTicketId(Long ticketId) throws DataAccessException {
        try {
            URI url = restClient.getServiceURI(String.format(TICKET_COUNT, ticketId));
            LOGGER.debug("Retrieving a news by id from {}", url);
            ResponseEntity<Integer> exchange =
                restClient.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Integer>() {
                    });
            LOGGER.debug("Result status was {} with content {}", exchange.getStatusCode(), exchange.getBody());
            return exchange.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("ticket.notfound"), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public TicketDTO reserve(TicketDTO ticketDTO) throws DataAccessException{
        try {
            URI url = restClient.getServiceURI(RESERVE_TICKET);
            LOGGER.debug("Retrieving a news by id from {}", url);

            HttpEntity<TicketDTO> ticketDTOHttpEntity = new HttpEntity<>(ticketDTO);
            ResponseEntity<TicketDTO> exchange =
                restClient.exchange(
                    url,
                    HttpMethod.PUT,
                    ticketDTOHttpEntity,
                    new ParameterizedTypeReference<TicketDTO>() {
                    });
            LOGGER.debug("Result status was {} with content {}", exchange.getStatusCode(), exchange.getBody());
            return exchange.getBody();
        } catch (HttpStatusCodeException e) {
            if(e.getStatusCode() == HttpStatus.FORBIDDEN){
                throw new DataAccessException(BundleManager.getExceptionBundle().getString("performance.started"));
            }else{
                throw new DataAccessException(BundleManager.getExceptionBundle().getString("ticket.cancelled"));
            }
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public TicketDTO buy(TicketDTO ticketDTO) throws DataAccessException{
        try {
            URI url = restClient.getServiceURI(BUY_TICKET);
            LOGGER.debug("Retrieving a news by id from {}", url);

            HttpEntity<TicketDTO> ticketDTOHttpEntity = new HttpEntity<>(ticketDTO);
            ResponseEntity<TicketDTO> exchange =
                restClient.exchange(
                    url,
                    HttpMethod.POST,
                    ticketDTOHttpEntity,
                    new ParameterizedTypeReference<TicketDTO>() {
                    });
            LOGGER.debug("Result status was {} with content {}", exchange.getStatusCode(), exchange.getBody());
            return exchange.getBody();
        } catch (HttpStatusCodeException e) {
            if(e.getStatusCode() == HttpStatus.FORBIDDEN){
                throw new DataAccessException(BundleManager.getExceptionBundle().getString("performance.started"));
            }else{
                throw new DataAccessException(BundleManager.getExceptionBundle().getString("ticket.cancelled"));
            }
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public void delete(TicketDTO ticketDTO) throws DataAccessException {
        try {
            URI url = restClient.getServiceURI(DELETE_TICKET);
            LOGGER.debug("Retrieving a news by id from {}", url);

            HttpEntity<TicketDTO> ticketDTOHttpEntity = new HttpEntity<>(ticketDTO);
            ResponseEntity<TicketDTO> exchange =
                restClient.exchange(
                    url,
                    HttpMethod.DELETE,
                    ticketDTOHttpEntity,
                    new ParameterizedTypeReference<TicketDTO>() {
                    });
            LOGGER.debug("Result status was {} with content {}", exchange.getStatusCode(), exchange.getBody());
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("ticket.notfound"), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }
}
