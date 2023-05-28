package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;

import java.util.List;

public interface NewsRestClient {

    /**
     * Find all news entries.
     *
     * @return list of news entries
     * @throws DataAccessException in case something went wrong
     */
    List<SimpleNewsDTO> findAll() throws DataAccessException;

    /**
     * Find a detailed news by id.
     * @param id news id
     * @return detailed news dto
     * @throws DataAccessException in case something went wrong
     */
    DetailedNewsDTO findOneById(Long id) throws DataAccessException;

    /**
     * Publish a news
     * @param detailedNewsDTO detailed news
     * @return detailed news dto
     * @throws DataAccessException in case something went wrong
     */
    DetailedNewsDTO publishNews(DetailedNewsDTO detailedNewsDTO) throws DataAccessException;

    /**
     * Find all unread news entries by user.
     * @param userID id of user
     * @return list of unread news entries
     * @throws DataAccessException in case something went wrong
     */
    List<SimpleNewsDTO> findAllUnreadNews(Long userID) throws DataAccessException;


    /**
     * Set status of a news to read for a user
     * @param newsId id of news
     * @throws DataAccessException in case something went wrong
     */
    void readNews(Long newsId) throws DataAccessException;

}
