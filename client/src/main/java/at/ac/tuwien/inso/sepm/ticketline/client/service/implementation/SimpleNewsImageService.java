package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ClientServiceValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.NewsImageRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsImageService;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.NewsImageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SimpleNewsImageService implements NewsImageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleNewsImageService.class);

    private final NewsImageRestClient newsImageRestClient;

    public SimpleNewsImageService(NewsImageRestClient newsImageRestClient) {
        this.newsImageRestClient = newsImageRestClient;
    }

    @Override
    public NewsImageDTO createImage(NewsImageDTO newsImageDTO) throws DataAccessException, ClientServiceValidationException {
        LOGGER.info("Creating image");
        if (newsImageDTO.getId() != null){
            throw new ClientServiceValidationException("News image is not valid!");
        }
        return newsImageRestClient.createImage(newsImageDTO);
    }

    @Override
    public NewsImageDTO findByOneId(Long id) throws DataAccessException, ClientServiceValidationException {
        LOGGER.info("Finding an image with id " + id);
        if (id == null){
            throw new ClientServiceValidationException("News image is not valid!");
        }
        return newsImageRestClient.findByOneId(id);
    }
}
