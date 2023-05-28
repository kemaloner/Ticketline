package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;

import java.util.List;

public interface NewsService {

    /**
     * Find all news entries ordered by published at date (descending).
     *
     * @return ordered list of all news entries
     */
    List<News> findAll();


    /**
     * Find a single news entry by id.
     *
     * @param id the is of the news entry
     * @return the news entry
     */
    News findOne(Long id);

    /**
     * Publish a single news entry
     *
     * @param news to publish
     * @return published news entry
     */
    News publishNews(News news);

    /**
     * Find all unread news for given user id ordered by published at date
     *
     * @param userId of the user
     * @return list of unread news
     */
    List<News> findUnreadNews(Long userId);

}
