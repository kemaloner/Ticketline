package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.artist.ArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;


public interface ArtistRestClient {

    /**
     * Finds a single artist entry by the given id
     * @param id id of the artist
     * @return A single artist entry
     * @throws DataAccessException in case something went wrong
     */
    ArtistDTO findOneById(Long id) throws DataAccessException;

    /**
     * Sends request to the server to find every artist entry in the given page
     * @param request Pageable request
     * @return A PaginationWrapper object containing results and pagination relevant information
     * @throws DataAccessException in case something went wrong
     */
    PaginationWrapper<ArtistDTO> findAll(Pageable request) throws DataAccessException;

    /**
     * Sends request to the server to find artist entries by the given criteria
     * @param params A Map containing information about which input fields have been used with which input
     * @param request Pageable request
     * @return A PaginationWrapper object containing results and pagination relevant information
     * @throws DataAccessException in case something went wrong
     */
    PaginationWrapper<ArtistDTO> findAdvanced(MultiValueMap<String, String> params, Pageable request) throws DataAccessException;

}
