package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.NewsImageDTO;

public interface NewsImageRestClient {

    /**
     * create an image
     * @param newsImageDTO image dto
     * @return image dto
     * @throws DataAccessException in case something went wrong
     */
    NewsImageDTO createImage(NewsImageDTO newsImageDTO) throws DataAccessException;

    /**
     * find an image by id
     * @param id id of image
     * @return dto of image
     * @throws DataAccessException in case something went wrong
     */
    NewsImageDTO findByOneId(Long id) throws DataAccessException;
}
