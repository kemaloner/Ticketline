package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.customer.CustomerMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/customer")
@Api(value = "customer")
public class CustomersEndPoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomersEndPoint.class);


    private final CustomerMapper customerMapper;
    private final CustomerService customerService;


    public CustomersEndPoint(CustomerMapper customerMapper, CustomerService customerService) {
        this.customerMapper = customerMapper;
        this.customerService = customerService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get list of customer entries")
    public PaginationWrapper<CustomerDTO> findAll(Pageable pageable) {
        LOGGER.info("Loading all customers");
        Page<Customer> page = customerService.findAll(pageable);
        return new PaginationWrapper<>(customerMapper.customerToCustomerDTO(page.getContent()), page.getTotalPages());
    }

    @RequestMapping(value = "/search/{keyword}", method = RequestMethod.GET)
    @ApiOperation(value = "Gets all customers with the given keyword")
    public PaginationWrapper<CustomerDTO> findByKeyword(@PathVariable String keyword, Pageable pageable) {
        LOGGER.info("Loading customers by filter");
        Page<Customer> page = customerService.findByKeyword(keyword, pageable);
        return new PaginationWrapper<>(customerMapper.customerToCustomerDTO(page.getContent()), page.getTotalPages());
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperation(value = "Save the given customer")
    public void createCustomer(@RequestBody CustomerDTO customer) {
        LOGGER.info("Creating a new customer");
        customerService.saveCustomer(customerMapper.customerDTOToCustomer(customer));
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ApiOperation(value = "update the given customer")
    public void updateCustomer(@RequestBody CustomerDTO customerDTO) {
        LOGGER.info("Updating a existing customer");
        customerService.updateCustomer(customerMapper.customerDTOToCustomer(customerDTO));
    }

    @RequestMapping(value = "/find/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get one customer by id")
    public CustomerDTO findOne(@PathVariable("id") Long id){
        LOGGER.info("Finding a customer with id " + id);
        return customerMapper.customerToCustomerDTO(customerService.findOne(id));
    }
}
