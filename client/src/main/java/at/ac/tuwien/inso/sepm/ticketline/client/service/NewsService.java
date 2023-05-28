package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ClientServiceValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;

import java.util.List;

public interface NewsService {

    /**
     * Find all news entries.
     *
     * @return list of news entries
     * @throws DataAccessException in case something went wrong
     */
    List<SimpleNewsDTO> findAll() throws DataAccessException;

    /**
     * Find a news by id
     * @param id id of news
     * @return dto of detailed news
     * @throws DataAccessException in case something went wrong
     */
    DetailedNewsDTO findOneById(Long id) throws DataAccessException, ClientServiceValidationException;

    /**
     * Publish a news
     * @param detailedNewsDTO dto of detailed news
     * @return dto of detailed news
     * @throws DataAccessException in case something went wrong
     */
    DetailedNewsDTO publishNews(DetailedNewsDTO detailedNewsDTO) throws DataAccessException, ClientServiceValidationException;

    /**
     * Find all unread news entries by user id.
     * @param userID id of user
     * @return list of unread news entries
     * @throws DataAccessException in case something went wrong
     */
    List<SimpleNewsDTO> findAllUnreadNews(Long userID) throws DataAccessException, ClientServiceValidationException;

    /**
     * Set status of a news to read for a user
     * @param newsId id of news
     * @throws DataAccessException in case something went wrong
     */
    void readNews(Long newsId) throws DataAccessException, ClientServiceValidationException;

}
