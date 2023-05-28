package at.ac.tuwien.inso.sepm.ticketline.server.unittest;


import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.ServerServiceValidationException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.implementation.SimpleCustomerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
public class CustomerTest {
    private static final Long Customer_ID1 = 1L;
    private static final String Customer_NAME1 = "Name_1";
    private static final String Customer_SURNAME1 = "Surmane_1";
    private static final LocalDate Customer_BIRTHDATE1 = LocalDate.of(2000,1,1);
    private static final String Customer_ADDRESS1 = "Address_1";
    private static final String Customer_EMAIL1 = "email_1@a.com";
    private static final String Customer_PHONENUMBER1 = "PhoneNumber_1";

    private static final Long Customer_ID2 = 2L;
    private static final String Customer_NAME2 = "Name_2";
    private static final String Customer_SURNAME2 = "Surmane_2";
    private static final LocalDate Customer_BIRTHDATE2 = LocalDate.of(1990,1,1);
    private static final String Customer_ADDRESS2 = "Address_2";
    private static final String Customer_EMAIL2 = "email_2@a.com";
    private static final String Customer_PHONENUMBER2 = "PhoneNumber_2";

    @MockBean
    private CustomerRepository customerRepository;

    //@Autowired
    private CustomerService customerService;

    private List<Customer> customersList;
    private Customer customer1, customer2;
    private Map<Long, Customer> customersMap;


    @Before
    public void setUp(){

        customerService = new SimpleCustomerService(customerRepository);
        customer1 = Customer.builder()
            .id(Customer_ID1)
            .firstname(Customer_NAME1)
            .surname(Customer_SURNAME1)
            .birthday(Customer_BIRTHDATE1)
            .address(Customer_ADDRESS1)
            .phoneNumber(Customer_PHONENUMBER1)
            .email(Customer_EMAIL1)
            .build();

        customer2 = Customer.builder()
            .id(Customer_ID2)
            .firstname(Customer_NAME2)
            .surname(Customer_SURNAME2)
            .birthday(Customer_BIRTHDATE2)
            .address(Customer_ADDRESS2)
            .phoneNumber(Customer_PHONENUMBER2)
            .email(Customer_EMAIL2)
            .build();

        customersList = new ArrayList<>();
        customersList.add(customer1);
        customersList.add(customer2);

        customersMap = new HashMap<>();
        customersMap.put(Customer_ID1, customer1);
        customersMap.put(Customer_ID2, customer2);

        BDDMockito.doAnswer(inv -> {
            Customer saveCustomers = inv.getArgument(0);
            if (saveCustomers.getId() == null){
                saveCustomers.setId(1L + customersMap.size());
            }
            customersMap.put(saveCustomers.getId(), saveCustomers);
            return saveCustomers;
        }).when(customerRepository).save(any(Customer.class));
    }

    @Test
    public void findAllCustomers(){
        PageRequest request = PageRequest.of(1,10);
        BDDMockito.
            given(customerRepository.findAll(request)).
            willReturn(new PageImpl<Customer>(customersList,request,customersList.size()));

        Page<Customer> page = customerService.findAll(request);

        assertEquals(customersList, page.getContent());
    }

    @Test
    public void findOneCustomer(){
        Long customerId = 1L;
        BDDMockito.
            given(customerRepository.findById(customerId)).
            willReturn(Optional.of(customer1));

        Customer actualCustomer = customerService.findOne(customerId);

        assertThat(customer1).isEqualTo(actualCustomer);
    }


    @Test
    public void findCustomersByKeyword(){
        PageRequest request = PageRequest.of(1,10);
        BDDMockito.
            given(customerRepository.findByKeyword(Customer_NAME1,request)).
            willReturn(new PageImpl<>(customersList,request,customersList.size()));


        Page<Customer> page = customerService.findByKeyword(Customer_NAME1,request);

        assertEquals(customersList, page.getContent());
    }


    @Test
    public void saveCustomer(){
        Customer customerExpected = Customer.builder()
            .id(3L)
            .firstname("Name_3")
            .surname("Surname_3")
            .birthday(LocalDate.of(2000,1,1))
            .address("Address_3")
            .phoneNumber("PhoneNumber_3")
            .email("Email_3@a.com")
            .build();

        Customer customerActual = customerService.saveCustomer(customerExpected);

        assertThat(customerExpected).isEqualTo(customerActual);
    }

    @Test(expected = ServerServiceValidationException.class)
    public void saveInvalidCustomer(){
        Customer customerExpected = Customer.builder()
            .id(3L)
            .firstname("Name_3")
            .surname("Surname_3")
            .birthday(LocalDate.of(2000,1,1))
            .address("Address_3")
            .phoneNumber("PhoneNumber_3")
            .email("Email_3a.com")
            .build();

        Customer customerActual = customerService.saveCustomer(customerExpected);

        assertThat(customerExpected).isEqualTo(customerActual);
    }


    @Test
    public void updateCustomer(){
        BDDMockito.
            given(customerRepository.findById(customer1.getId())).
            willReturn(Optional.of(customer1));

        Customer customerUpdate = Customer.builder()
            .id(customer1.getId())
            .firstname("Max")
            .surname(customer1.getSurname())
            .birthday(customer1.getBirthday())
            .address(customer1.getAddress())
            .phoneNumber(customer1.getPhoneNumber())
            .email(customer1.getEmail())
            .build();

        customerService.updateCustomer(customerUpdate);

        assertEquals("Max", customersMap.get(Customer_ID1).getFirstname());
    }


    @Test(expected = ServerServiceValidationException.class)
    public void updateInvalidCustomer(){
        BDDMockito.
            given(customerRepository.findById(customer1.getId())).
            willReturn(Optional.of(customer1));

        Customer customerUpdate = Customer.builder()
            .id(customer1.getId())
            .firstname("Max")
            .surname(customer1.getSurname())
            .birthday(LocalDate.of(2017,1,1))
            .address(customer1.getAddress())
            .phoneNumber(customer1.getPhoneNumber())
            .email(customer1.getEmail())
            .build();

        customerService.updateCustomer(customerUpdate);

        assertEquals("Max", customersMap.get(Customer_ID1).getFirstname());
    }

}
