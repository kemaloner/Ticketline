package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.InvoiceRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.invoice.InvoiceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

@Component
public class SimpleInvoiceRestClient implements InvoiceRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleInvoiceRestClient.class);
    private static final String INVOICE_URL = "/invoice";

    private final RestClient restClient;

    public SimpleInvoiceRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public InvoiceDTO findOneById(Long id) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving Invoice with id from {}", restClient.getServiceURI(INVOICE_URL)+"/findOneById/"+id);
            ResponseEntity<InvoiceDTO> exchange =
                restClient.exchange(
                    restClient.getServiceURI(INVOICE_URL+"/findOneById/"+id),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<InvoiceDTO>() {
                    });
            LOGGER.debug("Result status was {} with content {}", exchange.getStatusCode(), exchange.getBody());
            return exchange.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("invoice.notfound"), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public InvoiceDTO findByTicketId(Long ticket_id) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving Invoice for a ticket from {}", restClient.getServiceURI(INVOICE_URL)+"/findOneById/"+ticket_id);
            ResponseEntity<InvoiceDTO> exchange =
                restClient.exchange(
                    restClient.getServiceURI(INVOICE_URL+"/findOneByTicketId/"+ticket_id),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<InvoiceDTO>() {
                    });
            LOGGER.debug("Result status was {} with content {}", exchange.getStatusCode(), exchange.getBody());
            return exchange.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("invoice.notfound"), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }
}
