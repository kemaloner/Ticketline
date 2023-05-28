package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.UserRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SimpleUserService implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleUserService.class);


    private final UserRepository userRepository;

    public SimpleUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Find all user entries.
     *
     * @return ordered list of al user entries
     */
    @Override
    public Page<User> findAll(Pageable request) {
        LOGGER.info("Find all users");
        return userRepository.findAll(request);
    }

    /**
     * Find a single user entry by id.
     *
     * @param id the is of the user entry
     * @return the user entry
     */
    @Override
    public User findOne(Long id) {
        return userRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    /**
     * Find a single user entry by id.
     *
     * @param username the is of the user entry
     * @return the user entry
     */
    @Override
    public User findOneByUserName(String username) {
        LOGGER.info("Find a user by user name " + username);
        return userRepository.findOneByUserName(username).orElseThrow(NotFoundException::new);
    }

    /**
     * save user
     *
     * @param user to save
     * @return saved user
     */
    @Override
    public User save(User user) {
        LOGGER.info("Save a new user");
        return userRepository.save(user);
    }


    @Override
    public Page<User> findByKeyword(String keyword,Pageable request){
        LOGGER.info("Find users by filter");
        return userRepository.findByKeyword(keyword, request);
    }

    /**
     * Decrease Attemts a user
     *
     * @param user to decrease attemts
     */
    @Override
    public void decreaseAttemts(User user) {
        if (user.isActive() && user.getActiveTime().isBefore(LocalDateTime.now())) {
            int attempts = user.getAttempts() - 1;
            if (attempts <= 0) {
                user.setActiveTime(LocalDateTime.now().withNano(0).plusSeconds(600L));
            }
            user.setAttempts(attempts);
            save(user);
        }
    }

    /**
     * Reset Attemts a user
     *
     * @param user to reset attemts
     */
    @Override
    public void resetAttemts(User user) {
        if(!user.getAttempts().equals(User.ATTEMPTS) || !user.isActive()) {
            setActive(user, true);
        }
    }

    @Override
    public User setActive(User user, boolean active) {
        LOGGER.info("Setting activation status of a user by username " + user.getUserName());
        return setActive(user,active,-1L);
    }

    @Override
    public User setActive(User user, boolean active, Long second) {
        LOGGER.info("Setting activation status of a user by username " + user.getUserName());
        user.setActive(active);
        user.setAttempts(active ? User.ATTEMPTS : 0);
        user.setActiveTime(LocalDateTime.now().withNano(0).plusSeconds(second));
        return save(user);
    }

    @Override
    public void readNews(Long userId, News news) {
        User user = findOne(userId);
        if(!user.getReadNews().contains(news)){
            user.getReadNews().add(news);
            save(user);
        }
    }


}
