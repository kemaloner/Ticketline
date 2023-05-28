package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.ServerServiceValidationException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.NewsRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SimpleNewsService implements NewsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleNewsService.class);


    private final NewsRepository newsRepository;

    public SimpleNewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    public List<News> findAll() {
        LOGGER.info("Find all news");
        return newsRepository.findAllByOrderByPublishedAtDesc();
    }

    @Override
    public News findOne(Long id) {
        LOGGER.info("Find a news by id " + id);
        if (id == null){
            throw new ServerServiceValidationException("News is not valid!");
        }
        return newsRepository.findOneById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public News publishNews(News news) {
        LOGGER.info("Publishing news");
        if (validateNews(news)) {
            news.setPublishedAt(LocalDateTime.now());
            return newsRepository.save(news);
        } else {
            throw new ServerServiceValidationException("News is not valid!");
        }
    }

    @Override
    public List<News> findUnreadNews(Long userId) {
        LOGGER.info("All unread news are loading");
        if (userId == null){
            throw new ServerServiceValidationException("User is not valid!");
        }
        return newsRepository.findUnreadNews(userId);
    }

    private boolean validateNews(News news){
        if (news.getId() != null){
            return false;
        }
        if (news.getText() == null || news.getText().trim().isEmpty()){
            return false;
        }
        if (news.getTitle() == null || news.getTitle().trim().isEmpty()){
            return false;
        }
        return true;
    }
}
