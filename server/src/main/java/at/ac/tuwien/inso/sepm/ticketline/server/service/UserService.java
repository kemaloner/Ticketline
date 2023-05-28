package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    /**
     * @param request pageRequest
     * @return List of user entries
     */
    Page<User> findAll(Pageable request);

    /**
     * Find a single user entry by id.
     *
     * @param id the is of the user entry
     * @return the user entry
     */
    User findOne(Long id);


    /**
     * Find a single user entry by id.
     *
     * @param username the is of the user entry
     * @return the user entry
     */
    User findOneByUserName(String username);

    /**
     * Find user with keyword
     * @param keyword search input
     * @return found user
     */
    Page<User> findByKeyword(String keyword, Pageable request);

    /**
     * save user
     *
     * @param user to save
     * @return saved user
     */
    User save(User user);


    /**
     * Decrease Attemts a user
     * @param user to decrease attemts
     */
    void decreaseAttemts(User user);


    /**
     * Reset Attems a user
     * @param user to reset attemts
     */
    void resetAttemts(User user);

    /**

     * Set user active/deactive
     * @param user User, whose status will be changed
     * @param active status
     * @return user
     */
    User setActive(User user, boolean active);

     /* Set user active/deactive
     * @param user User, whose status will be changed
     * @param active status
     * @param second when the user will be unlocked, after being locked
     * @return user
     */
    User setActive(User user, boolean active, Long second);

    /**
     * Mark the read news
     * @param userId id of user
     * @param news id of news
     */
    void readNews(Long userId, News news);


}
