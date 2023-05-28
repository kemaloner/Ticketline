package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.UserRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;
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
public class SimpleUserRestClient implements UserRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleUserRestClient.class);
    private static final String USER_URL = "/user";
    private static final String USER_SEARCH_URL = USER_URL + "/search/%s";
    private static final String USER_ACTIVE_URL = USER_URL + "/active/%s/%b";

    private final RestClient restClient;

    public SimpleUserRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public PaginationWrapper<SimpleUserDTO> findAll(Pageable request) throws DataAccessException {
        try {
            URI url = restClient.getServiceURI(USER_URL, request);
            LOGGER.debug("Retrieving all user from {}", url);
            ResponseEntity<PaginationWrapper<SimpleUserDTO>> users =
                restClient.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<PaginationWrapper<SimpleUserDTO>>() {});
            LOGGER.debug("Result status was {} with content {}", users.getStatusCode(), users.getBody());
            return users.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("user.notfound"), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public DetailedUserDTO findOneByUserName(String username) throws DataAccessException {
        try {
            URI url = restClient.getServiceURI(USER_URL + "/" + username);
            LOGGER.debug("Retrieving user from {}", url);
            ResponseEntity<DetailedUserDTO> response =
                restClient.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<DetailedUserDTO>() {});
            LOGGER.debug("Result status was {} with content {}", response.getStatusCode(), response.getBody());
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("user.notfound"), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public PaginationWrapper<SimpleUserDTO> findByKeyword(String keyword, Pageable request) throws DataAccessException {
        try {
            URI url = restClient.getServiceURI(String.format(USER_SEARCH_URL,keyword), request);
            LOGGER.debug("Retrieving all user from {}", url);
            ResponseEntity<PaginationWrapper<SimpleUserDTO>> users =
                restClient.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<PaginationWrapper<SimpleUserDTO>>(){});
            LOGGER.debug("Result status was {} with content {}", users.getStatusCode(), users.getBody());
            return users.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("user.notfound"), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public DetailedUserDTO save(DetailedUserDTO user) throws DataAccessException {
        try {
            URI url = restClient.getServiceURI(USER_URL);
            LOGGER.info("Save {} at {}", user.getUserName(), url);
            ResponseEntity<DetailedUserDTO> response =
                restClient.exchange(
                    url,
                    HttpMethod.PUT,
                    new HttpEntity<Object>(user),
                    new ParameterizedTypeReference<DetailedUserDTO>() {});
            LOGGER.debug("Save {} status {}", user.getUserName(), response.getStatusCode());
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("user.save"), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public SimpleUserDTO setActive(String username, boolean active) throws DataAccessException {
        try {
            URI url = restClient.getServiceURI(String.format(USER_ACTIVE_URL, username, active));
            LOGGER.info("User.setActive at {}", url);
            ResponseEntity<SimpleUserDTO> response = restClient.exchange(
                url,
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<SimpleUserDTO>(){});
            LOGGER.debug("User.setActive {} status {}", username, response.getStatusCode());
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("user.active"), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }
}
