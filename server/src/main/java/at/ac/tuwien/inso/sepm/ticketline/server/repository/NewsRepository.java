package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    /**
     * Find a single news entry by id.
     *
     * @param id the is of the news entry
     * @return Optional containing the news entry
     */
    Optional<News> findOneById(Long id);

    /**
     * Find all news entries ordered by published at date (descending).
     *
     * @return ordered list of al news entries
     */
    List<News> findAllByOrderByPublishedAtDesc();

    /**
     * Find all unread news for given user id ordered by published at date (descending).
     *
     * @param userId of the user
     * @return ordered list of all unread news entries
     */
    @Query(value = "select * from news n where n.id not in" +
        "(select e.id from news e join read_News r on e.id = r.news_id where r.users_id = :user_id)" +
        "order by n.published_at desc", nativeQuery = true)
    List<News> findUnreadNews(@Param("user_id") Long userId);

}
