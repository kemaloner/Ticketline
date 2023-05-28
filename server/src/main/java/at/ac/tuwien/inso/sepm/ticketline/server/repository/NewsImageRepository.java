package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.NewsImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsImageRepository extends JpaRepository<NewsImage, Long> {

    /**
     * Find single image by id.
     *
     * @param id  id of news image
     * @return Optional containing the news image
     */
    Optional<NewsImage> findOneById(Long id);


}
