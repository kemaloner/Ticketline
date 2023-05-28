package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a single user entry by username.
     *
     * @param username the is of the user entry
     * @return Optional containing the user entry
     */
    Optional<User> findOneByUserName(String username);


    /**
     * Find user with keyword
     * @param keyword search input
     * @return found user
     */
    @Query(value = "SELECT * FROM USERS WHERE UPPER(CONCAT(FIRSTNAME,' ',LASTNAME, ' ', USERNAME)) LIKE CONCAT('%', UPPER(:keyword), '%')", nativeQuery = true)
    Page<User> findByKeyword(@Param("keyword") String keyword, Pageable request);

}
