package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ClientServiceValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    /**
     * Find all Customer entries.
     *
     * @return list of customer entries
     * @throws DataAccessException in case something went wrong
     * @param request Pageable request
     */
    PaginationWrapper<CustomerDTO> findAll(Pageable request) throws DataAccessException;

    /**
     * Find customer entries by Keyword
     *
     * @param name of Customer entry
     * @return list of customer entries
     * @throws DataAccessException in case something went wrong
     */
    PaginationWrapper<CustomerDTO> findByKeyword(String name, Pageable request) throws DataAccessException;

    /**
     * Save Customer to Database.
     *
     * @param customer Customer for save
     * @throws DataAccessException in case something went wrong
     */
    void saveCustomer(CustomerDTO customer) throws DataAccessException;

    /**
     * Update Customer in Database
     *
     * @param customer Customer for update.
     * @throws DataAccessException in case something went wrong
     */
    void updateCustomer(CustomerDTO customer) throws DataAccessException;

    /**
     * Validate Customer
     *
     * @param customer Customer for validate
     * @throws ClientServiceValidationException if Customer invalid
     */
    void validateCustomer(CustomerDTO customer) throws ClientServiceValidationException;

    /**
     * Find a customer by the given id
     * @param id Customers id
     * @return Customer object when found, null otherwise
     * @throws DataAccessException in case something went wrong
     */
    CustomerDTO findOne(Long id) throws DataAccessException;


}
