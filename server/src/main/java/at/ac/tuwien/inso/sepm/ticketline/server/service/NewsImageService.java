package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.NewsImage;

import java.io.IOException;
import java.util.List;

public interface NewsImageService {

    /**
     * Saves a new NewsImage object into the database
     * @param newsImage to save in the database
     * @return the created news image
     * @throws IOException in case file could not be written
     */
    NewsImage createNewsImage(NewsImage newsImage) throws IOException;

    /**
     * Finds a NewsImage by id
     * @param id of the searched news image
     * @return the news image with given id
     */
    NewsImage findByOneId(Long id);

    /**
     * Find all news image entries
     * @return list of all news image entries
     */
    List<NewsImage> findAll();
}
