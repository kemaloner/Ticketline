package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ClientServiceValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.NewsImageDTO;

public interface NewsImageService {

    /**
     * create an image
     * @param newsImageDTO dto of image
     * @return dto of image
     * @throws DataAccessException in case something went wrong
     * @throws ClientServiceValidationException if dto is not valid
     */
    NewsImageDTO createImage(NewsImageDTO newsImageDTO) throws DataAccessException, ClientServiceValidationException;

    /**
     * find an image by id
     * @param id if of image
     * @return dto of image
     * @throws DataAccessException in case something went wrong
     * @throws ClientServiceValidationException if dto could not be found
     */
    NewsImageDTO findByOneId(Long id) throws DataAccessException, ClientServiceValidationException;
}
