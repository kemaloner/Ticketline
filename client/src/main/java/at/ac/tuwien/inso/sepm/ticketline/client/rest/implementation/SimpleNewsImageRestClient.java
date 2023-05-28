package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.NewsImageRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.NewsImageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

@Component
public class SimpleNewsImageRestClient implements NewsImageRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleNewsImageRestClient.class);
    private static final String NEWS_IMAGE_URL = "/newsImage";

    private final RestClient restClient;

    public SimpleNewsImageRestClient(RestClient restClient) {
        this.restClient = restClient;
    }


    @Override
    public NewsImageDTO createImage(NewsImageDTO newsImageDTO) throws DataAccessException {
        try {
            LOGGER.debug("Create an image with {}", restClient.getServiceURI(NEWS_IMAGE_URL));
            HttpEntity<NewsImageDTO> httpEntity = new HttpEntity<>(newsImageDTO);
            ResponseEntity<NewsImageDTO> news =
                restClient.exchange(
                    restClient.getServiceURI(NEWS_IMAGE_URL),
                    HttpMethod.POST,
                    httpEntity,
                    new ParameterizedTypeReference<NewsImageDTO>() {
                    });
            LOGGER.debug("Result status was {} with content {}", news.getStatusCode(), news.getBody());
            return news.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("newsimage.save"), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }

    @Override
    public NewsImageDTO findByOneId(Long id) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving a image by id from {}", restClient.getServiceURI(NEWS_IMAGE_URL));
            ResponseEntity<NewsImageDTO> news =
                restClient.exchange(
                    restClient.getServiceURI(NEWS_IMAGE_URL+"/"+id),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<NewsImageDTO>() {
                    });
            LOGGER.debug("Result status was {} with content {}", news.getStatusCode(), news.getBody());
            return news.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("newsimage.notfound"), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.cannot_connect_to_server"), e);
        }
    }
}
