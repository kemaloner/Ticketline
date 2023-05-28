package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ClientServiceValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.NewsRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimpleNewsService implements NewsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleNewsService.class);

    private final NewsRestClient newsRestClient;

    public SimpleNewsService(NewsRestClient newsRestClient) {
        this.newsRestClient = newsRestClient;
    }

    @Override
    public List<SimpleNewsDTO> findAll() throws DataAccessException {
        LOGGER.info("Find all news");
        return newsRestClient.findAll();
    }

    @Override
    public DetailedNewsDTO findOneById(Long id) throws DataAccessException, ClientServiceValidationException {
        LOGGER.info("Find a news by id " + id);
        if (id == null){
            throw new ClientServiceValidationException("News is not valid!");
        }
        return newsRestClient.findOneById(id);
    }

    @Override
    public DetailedNewsDTO publishNews(DetailedNewsDTO detailedNewsDTO) throws DataAccessException, ClientServiceValidationException {
        LOGGER.info("Publishing news");
        validateNews(detailedNewsDTO);
        return newsRestClient.publishNews(detailedNewsDTO);
    }

    @Override
    public List<SimpleNewsDTO> findAllUnreadNews(Long userID) throws DataAccessException, ClientServiceValidationException {
        LOGGER.info("All unread news are loading");
        if (userID == null){
            throw new ClientServiceValidationException("User is not valid!");
        }
        return newsRestClient.findAllUnreadNews(userID);
    }

    @Override
    public void readNews(Long newsId) throws DataAccessException, ClientServiceValidationException {
        LOGGER.info("All news are loading");
        if (newsId == null){
            throw new ClientServiceValidationException("News is not valid!");
        }
        newsRestClient.readNews(newsId);
    }

    private void validateNews(DetailedNewsDTO news) throws ClientServiceValidationException{

        if (news.getText() == null || news.getText().equals("")){
            throw new ClientServiceValidationException(BundleManager.getExceptionBundle().getString("news.text.empty"));
        }
        if (news.getTitle() == null || news.getTitle().equals("")){
            throw new ClientServiceValidationException(BundleManager.getExceptionBundle().getString("news.title.empty"));
        }
        if(news.getTitle().length() > 100){
            throw new ClientServiceValidationException(BundleManager.getExceptionBundle().getString("news.title.length"));
        }
        if(news.getText().length() > 500){
            throw new ClientServiceValidationException(BundleManager.getExceptionBundle().getString("news.text.length"));
        }
    }
}
