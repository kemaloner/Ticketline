package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import org.springframework.data.domain.Pageable;

public interface CustomerRestClient {

    /**
     * Find all customer entries.
     *
     * @return list of customer entries
     * @throws DataAccessException in case something went wrong
     */
    PaginationWrapper<CustomerDTO> findAll(Pageable request) throws DataAccessException;

    /**
     * Find customer entries by keyword
     *
     * @param name of Customer entry
     * @return page of customer entries
     * @throws DataAccessException in case something went wrong
     */
    PaginationWrapper<CustomerDTO> findByKeyword(String name, Pageable request) throws DataAccessException;

    /**
     * Save Customer to Database.
     *
     * @param customerDTO Customer for save
     * @throws DataAccessException in case something went wrong
     */
    void saveCustomer(CustomerDTO customerDTO) throws DataAccessException;

    /**
     * Update Customer in Database
     *
     * @param customer Customer for update
     * @throws DataAccessException in case something went wrong
     */
    void updateCustomer(CustomerDTO customer) throws DataAccessException;

    /**
     * Find a single customer by the given id
     * @param id id of the customer
     * @return CustomerDTO object
     * @throws DataAccessException in case something went wrong
     */
    CustomerDTO findOne(Long id) throws DataAccessException;

}
