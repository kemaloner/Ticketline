package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import org.springframework.data.domain.Pageable;

public interface UserRestClient {

    /**
     * Find all user entries.
     *
     * @return ordered list of al user entries
     */
    PaginationWrapper<SimpleUserDTO> findAll(Pageable request) throws DataAccessException;

    /**
     * Find a single user entry by id.
     *
     * @param username the is of the user entry
     * @return the user entry
     */
    DetailedUserDTO findOneByUserName(String username) throws DataAccessException;

    /**
     * Find user with keyword
     * @param keyword by which a user will be found
     * @return found user
     */
    PaginationWrapper<SimpleUserDTO> findByKeyword(String keyword,Pageable request) throws DataAccessException;

    /**
     * save user
     *
     * @param user to save
     * @return saved user
     */
    DetailedUserDTO save(DetailedUserDTO user) throws DataAccessException;


    /**
     * Set user active/deactive
     * @param username of the user
     * @param active status of the user
     * @return saved user
     */
    SimpleUserDTO setActive(String username, boolean active) throws DataAccessException;


}
