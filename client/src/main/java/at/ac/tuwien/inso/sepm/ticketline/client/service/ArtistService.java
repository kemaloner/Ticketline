package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.artist.ArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;


public interface ArtistService {

    /**
     * Finds a single artist entry by the given id
     * @param id id of the artist
     * @return an artist entry
     * @throws DataAccessException in case something went wrong
     */
    ArtistDTO findOneById(Long id) throws DataAccessException;

    /**
     * Finds every artist entry in the given page
     * @param request Pageable request
     * @return A PaginationWrapper object containing results and pagination relevant information
     * @throws DataAccessException in case something went wrong
     */
    PaginationWrapper<ArtistDTO> findAll(Pageable request) throws DataAccessException;

    /**
     * Finds artist entries by the given criteria
     * @param params A Map containing information about which input fields have been used with which input
     * @param request Pageable request
     * @return A PaginationWrapper object containing results and pagination relevant information
     * @throws DataAccessException in case something went wrong
     */
    PaginationWrapper<ArtistDTO> findAdvanced(MultiValueMap<String, String> params, Pageable request) throws DataAccessException;
}
