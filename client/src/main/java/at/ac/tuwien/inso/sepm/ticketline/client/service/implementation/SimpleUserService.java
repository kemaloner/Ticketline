package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.UserRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SimpleUserService implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleUserService.class);

    private final UserRestClient userRestClient;
    private DetailedUserDTO currentUser;

    public SimpleUserService(UserRestClient userRestClient) {
        this.userRestClient = userRestClient;
    }

    @Override
    public PaginationWrapper<SimpleUserDTO> findAll(Pageable request) throws DataAccessException {
        LOGGER.info("Find all users");
        return userRestClient.findAll(request);
    }

    @Override
    public DetailedUserDTO findOneByUserName(String username) throws DataAccessException {
        LOGGER.info("Find a user by user name " + username);
        return userRestClient.findOneByUserName(username);
    }

    @Override
    public PaginationWrapper<SimpleUserDTO> findByKeyword(String keyword,Pageable request) throws DataAccessException {
        LOGGER.info("Find users by filter");
        return userRestClient.findByKeyword(keyword, request);
    }

    @Override
    public DetailedUserDTO save(DetailedUserDTO user) throws DataAccessException {
        LOGGER.info("Save a new user");
        return userRestClient.save(user);
    }

    @Override
    public SimpleUserDTO setActive(String username, boolean active) throws DataAccessException {
        LOGGER.info("Setting activation status of a user by username " + username);
        return userRestClient.setActive(username, active);
    }

    @Override
    public void setCurrentUserByUserName(String username) throws DataAccessException {
        LOGGER.info("Setting current user by username " + username);
        currentUser = userRestClient.findOneByUserName(username);
    }

    @Override
    public DetailedUserDTO getCurrentUser() {
        return currentUser;
    }

    @Override
    public boolean isAdmin() {
        return getCurrentUser().getRole() == 1;
    }
}
