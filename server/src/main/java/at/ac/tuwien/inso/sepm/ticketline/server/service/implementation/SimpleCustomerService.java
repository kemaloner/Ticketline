package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.EmptyFieldException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.ServerServiceValidationException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.regex.Pattern;

@Service
public class SimpleCustomerService implements CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleCustomerService.class);

    private final static Pattern mailPattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    private final CustomerRepository customerRepository;

    public SimpleCustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Page<Customer> findAll(Pageable request) {
        LOGGER.info("Loading all customers");
        return customerRepository.findAll(request);
    }

    @Override
    public Customer findOne(Long id) throws NotFoundException {
        LOGGER.info("Find a customer by id " + id);
        return customerRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public Customer saveCustomer(Customer customer) throws NotFoundException {
        LOGGER.info("Saving a new customer");
        validateCustomer(customer);
        return customerRepository.save(customer);
    }

    @Override
    public void updateCustomer(Customer customer) throws NotFoundException {
        LOGGER.info("Updating a customer");
        validateCustomer(customer);
        findOne(customer.getId());
        customerRepository.save(customer);
    }

    @Override
    public Page<Customer> findByKeyword(String name, Pageable request) {
        LOGGER.info("Loading customers by filter");
       return customerRepository.findByKeyword(name,request);
    }


    private void validateCustomer(Customer c) throws EmptyFieldException, ServerServiceValidationException{
        if(c.getFirstname() == null || c.getFirstname().trim().isEmpty()){
            throw new EmptyFieldException("firstname");
        }

        if(c.getSurname() == null || c.getSurname().trim().isEmpty()){
            throw new EmptyFieldException("surname");
        }

        if(c.getAddress() == null || c.getAddress().trim().isEmpty()){
            throw new EmptyFieldException("adress");
        }

        if(c.getEmail() == null || c.getEmail().trim().isEmpty()){
            throw new EmptyFieldException("email");
        }
        else if(!mailPattern.matcher(c.getEmail()).matches()){
            throw new ServerServiceValidationException("mail is not valid!");
        }

        if(c.getPhoneNumber() == null || c.getPhoneNumber().trim().isEmpty()){
            throw new EmptyFieldException("phonenumber");
        }

        if(c.getBirthday() == null){
            throw new EmptyFieldException("birthday");
        }
        else if(c.getBirthday().isAfter(LocalDate.now().minusYears(16))){
            throw new ServerServiceValidationException("birthday is not valid!");
        }
    }
}
