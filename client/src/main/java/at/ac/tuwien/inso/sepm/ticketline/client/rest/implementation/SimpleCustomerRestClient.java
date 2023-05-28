package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.CustomerRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.net.URI;

@Component
public class SimpleCustomerRestClient implements CustomerRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleCustomerRestClient.class);
    private static final String CUSTOMER_URL = "/customer";
    private static final String CUSTOMER_SEARCH_URL = CUSTOMER_URL + "/search/";


    private final RestClient restClient;

    public SimpleCustomerRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public PaginationWrapper<CustomerDTO> findAll(Pageable request) throws DataAccessException {
        try {
            URI url = restClient.getServiceURI(CUSTOMER_URL, request);
            LOGGER.debug("Retrieving all customers from {}", url);
            ResponseEntity<PaginationWrapper<CustomerDTO>> customers =
                restClient.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<PaginationWrapper<CustomerDTO>>() {});
            LOGGER.debug("Result status was {} with content {}", customers.getStatusCode(), customers.getBody());
            return customers.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("customer.notfound"), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public PaginationWrapper<CustomerDTO> findByKeyword(String keyword, Pageable request) throws DataAccessException {
        try {
            URI url = restClient.getServiceURI(CUSTOMER_SEARCH_URL+keyword,request);
            LOGGER.debug("Retrieving found by name custumers from {}", restClient.getServiceURI(CUSTOMER_SEARCH_URL));
            ResponseEntity<PaginationWrapper<CustomerDTO>> customers =
                restClient.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<PaginationWrapper<CustomerDTO>>(){});
            LOGGER.debug("Result status was {} with content {}", customers.getStatusCode(), customers.getBody());
            return customers.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("customer.notfound"), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public void saveCustomer(CustomerDTO customerDTO) throws DataAccessException {
        try {
            LOGGER.debug("Save customer "+customerDTO.toString());
            HttpEntity<CustomerDTO> entity = new HttpEntity<>(customerDTO);
            restClient.exchange(
                restClient.getServiceURI(CUSTOMER_URL+"/save"),
                HttpMethod.POST,
                entity,
                Void.class);
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("customer.save"), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public void updateCustomer(CustomerDTO customer) throws DataAccessException {
        try {
            LOGGER.debug("Update customer ");
            HttpEntity<CustomerDTO> entity = new HttpEntity<>(customer);
            restClient.exchange(
                restClient.getServiceURI(CUSTOMER_URL+"/update"),
                HttpMethod.PUT,
                entity,
                Void.class);
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("customer.update"), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public CustomerDTO findOne(Long id) throws DataAccessException {
        try{
            URI url = restClient.getServiceURI(CUSTOMER_URL+"/find/"+id);
            LOGGER.debug("Find Customer by id from {}");
            ResponseEntity<CustomerDTO> customer =
                restClient.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<CustomerDTO>() {
                    }
                );

            LOGGER.debug("Result status was {} with content {}", customer.getStatusCode(), customer.getBody());
            return customer.getBody();
        }catch (HttpStatusCodeException e){
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("customer.notfound"), e);
        }catch (RestClientException e){
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }
}
