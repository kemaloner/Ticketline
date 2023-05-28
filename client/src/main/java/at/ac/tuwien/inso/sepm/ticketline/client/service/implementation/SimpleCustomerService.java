package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ClientServiceValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.CustomerRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SimpleCustomerService implements CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleCustomerService.class);

    private final CustomerRestClient customerRestClient;

    public SimpleCustomerService(CustomerRestClient customerRestClient) {
        this.customerRestClient = customerRestClient;
    }

    @Override
    public PaginationWrapper<CustomerDTO> findAll(Pageable request) throws DataAccessException {
        LOGGER.info("Loading all customers");
        return customerRestClient.findAll(request);
    }

    @Override
    public PaginationWrapper<CustomerDTO> findByKeyword(String name, Pageable request) throws DataAccessException {
        LOGGER.info("Loading customers by filter");
        return customerRestClient.findByKeyword(name, request);
    }

    @Override
    public void saveCustomer(CustomerDTO customer) throws DataAccessException {
        LOGGER.info("Saving a new customer");
        customerRestClient.saveCustomer(customer);
    }

    @Override
    public void updateCustomer(CustomerDTO customer) throws DataAccessException {
        LOGGER.info("Updating a customer");
        customerRestClient.updateCustomer(customer);
    }

    @Override
    public void validateCustomer(CustomerDTO customer) throws ClientServiceValidationException {

        if(customer.getFirstname().isEmpty())
            throw new ClientServiceValidationException(BundleManager.getBundle().getString("dialog.customer.error.firstname.empty"));

        if(customer.getSurname().isEmpty())
            throw new ClientServiceValidationException(BundleManager.getBundle().getString("dialog.customer.error.surname.empty"));

        if(customer.getEmail().isEmpty())
            throw new ClientServiceValidationException(BundleManager.getBundle().getString("dialog.customer.error.email.empty"));


        boolean emailValid;
        boolean birthdayValid;


        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(customer.getEmail());
        emailValid = matcher.matches();

        if(!emailValid){
            throw new ClientServiceValidationException(BundleManager.getBundle().getString("dialog.customer.error.email.format"));
        }

        if(customer.getBirthday() == null){
            throw new ClientServiceValidationException(BundleManager.getBundle().getString("dialog.customer.error.birthday.empty"));
        }

        if(customer.getAddress().isEmpty()){
            throw new ClientServiceValidationException(BundleManager.getBundle().getString("dialog.customer.error.adress.empty"));
        }

        if(customer.getPhoneNumber().isEmpty()){
            throw new ClientServiceValidationException(BundleManager.getBundle().getString("dialog.customer.error.phone.empty"));
        }

        LocalDate date = LocalDate.now().minusYears(16);
        birthdayValid = (customer.getBirthday().isBefore(date) || customer.getBirthday().isEqual(date));

        if(!birthdayValid)
            throw new ClientServiceValidationException(BundleManager.getBundle().getString("dialog.customer.error.birthday.invalid"));


    }

    @Override
    public CustomerDTO findOne(Long id) throws DataAccessException {
        LOGGER.info("Find a customer by id " + id);
        return customerRestClient.findOne(id);
    }
}
