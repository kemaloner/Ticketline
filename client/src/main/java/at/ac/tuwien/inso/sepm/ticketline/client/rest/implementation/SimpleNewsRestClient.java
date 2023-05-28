package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.NewsRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.net.URI;
import java.util.List;

@Component
public class SimpleNewsRestClient implements NewsRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleNewsRestClient.class);
    private static final String NEWS_URL = "/news";
    private static final String NEWS_READ_URL = NEWS_URL + "/read/%d";

    private final RestClient restClient;

    public SimpleNewsRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public List<SimpleNewsDTO> findAll() throws DataAccessException {
        try {
            LOGGER.debug("Retrieving all news from {}", restClient.getServiceURI(NEWS_URL));
            ResponseEntity<List<SimpleNewsDTO>> news =
                restClient.exchange(
                    restClient.getServiceURI(NEWS_URL),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<SimpleNewsDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", news.getStatusCode(), news.getBody());
            return news.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("news.notfound"), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public DetailedNewsDTO findOneById(Long id) throws DataAccessException{
        try {
            LOGGER.debug("Retrieving a news by id from {}", restClient.getServiceURI(NEWS_URL));
            ResponseEntity<DetailedNewsDTO> news =
                restClient.exchange(
                    restClient.getServiceURI(NEWS_URL+"/"+id),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<DetailedNewsDTO>() {
                    });
            LOGGER.debug("Result status was {} with content {}", news.getStatusCode(), news.getBody());
            return news.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("news.notfound"), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public DetailedNewsDTO publishNews(DetailedNewsDTO detailedNewsDTO) throws DataAccessException {
        try {
            LOGGER.debug("Publish a news with {}", restClient.getServiceURI(NEWS_URL));
            HttpEntity<DetailedNewsDTO> httpEntity = new HttpEntity<>(detailedNewsDTO);
            ResponseEntity<DetailedNewsDTO> news =
                restClient.exchange(
                    restClient.getServiceURI(NEWS_URL),
                    HttpMethod.POST,
                    httpEntity,
                    new ParameterizedTypeReference<DetailedNewsDTO>() {
                    });
            LOGGER.debug("Result status was {} with content {}", news.getStatusCode(), news.getBody());
            return news.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("news.add.alert.header"), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public List<SimpleNewsDTO> findAllUnreadNews(Long userID) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving all unread news from {}", restClient.getServiceURI(NEWS_URL));
            ResponseEntity<List<SimpleNewsDTO>> news =
                restClient.exchange(
                    restClient.getServiceURI(NEWS_URL+"/unreadnews/"+userID),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<SimpleNewsDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", news.getStatusCode(), news.getBody());
            return news.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("news.notfound"), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public void readNews(Long newsId) throws DataAccessException {
        try {
            URI url =  restClient.getServiceURI(String.format(NEWS_READ_URL, newsId));

            LOGGER.debug("Read a news with {}", url);
            ResponseEntity<Void> news =
                restClient.exchange(url,HttpMethod.POST, null, Void.class);
            LOGGER.debug("Result status was {} with content {}", news.getStatusCode(), news.getBody());
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("news.read"), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

}
