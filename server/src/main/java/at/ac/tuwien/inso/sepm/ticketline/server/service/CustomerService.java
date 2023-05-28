package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    /**
     * Gets every customer entry from the database
     * @param request pageRequest
     * @return Page of costumers
     */
    Page<Customer> findAll(Pageable request);

    /**
     * Find a single customer entry by id.
     * @param id the is of the customer entry
     * @return the customer entry
     */
    Customer findOne(Long id);

    /**
     * Saves a new customer into the database
     * @param customer the customer to save in the database
     * @return the created customer with its generated id
     */
    Customer saveCustomer(Customer customer) throws NotFoundException;

    /**
     * Updates a customer with new credentials
     * @param customer the customer to be updated
     * @throws NotFoundException throw when Customer not found
     */
    void updateCustomer(Customer customer) throws NotFoundException;

    /**
     * Finds customers which conform to the given search criteria
     * @param name Customer's firstname
     * @param request pageRequest
     * @return Page of costumers
     */
    Page<Customer> findByKeyword(String name, Pageable request);

}
