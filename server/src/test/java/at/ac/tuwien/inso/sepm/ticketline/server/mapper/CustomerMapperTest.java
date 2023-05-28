package at.ac.tuwien.inso.sepm.ticketline.server.mapper;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.customer.CustomerMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class CustomerMapperTest {

    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper")
    public static class CustomerMapperTestContextConfiguration {
    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private CustomerMapper customerMapper;

    private static final Long CUSTOMER_ID = 1L;
    private static final String FIRSTNAME = "John";
    private static final String SURNAME = "Smith";
    private static final LocalDate BIRTHDAY = LocalDate.of(1989, 6, 30);
    private static final String ADDRESS = "Wiedner Hauptstrasse 76, 1040";
    private static final String EMAIL = "john.smith@tuwien.ac.at";
    private static final String PHONE_NUMBER = "012345678910";

    @Test
    public void shouldMapCustomerToCustomerDTO(){
        Customer customer = Customer.builder()
            .id(CUSTOMER_ID)
            .firstname(FIRSTNAME)
            .surname(SURNAME)
            .birthday(BIRTHDAY)
            .address(ADDRESS)
            .email(EMAIL)
            .phoneNumber(PHONE_NUMBER)
            .build();

        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
        assertThat(customerDTO).isNotNull();
        assertThat(customerDTO.getId()).isEqualTo(CUSTOMER_ID);
        assertThat(customerDTO.getFirstname()).isEqualTo(FIRSTNAME);
        assertThat(customerDTO.getSurname()).isEqualTo(SURNAME);
        assertThat(customerDTO.getBirthday()).isEqualTo(BIRTHDAY);
        assertThat(customerDTO.getAddress()).isEqualTo(ADDRESS);
        assertThat(customerDTO.getEmail()).isEqualTo(EMAIL);
        assertThat(customerDTO.getPhoneNumber()).isEqualTo(PHONE_NUMBER);
    }

    @Test
    public void shouldMapCustomerDTOToCustomer(){
        CustomerDTO customerDTO = CustomerDTO.builder()
            .id(CUSTOMER_ID)
            .firstname(FIRSTNAME)
            .surname(SURNAME)
            .birthday(BIRTHDAY)
            .address(ADDRESS)
            .email(EMAIL)
            .phoneNumber(PHONE_NUMBER)
            .build();

        Customer customer = customerMapper.customerDTOToCustomer(customerDTO);
        assertThat(customer).isNotNull();
        assertThat(customer.getId()).isEqualTo(CUSTOMER_ID);
        assertThat(customer.getFirstname()).isEqualTo(FIRSTNAME);
        assertThat(customer.getSurname()).isEqualTo(SURNAME);
        assertThat(customer.getBirthday()).isEqualTo(BIRTHDAY);
        assertThat(customer.getAddress()).isEqualTo(ADDRESS);
        assertThat(customer.getEmail()).isEqualTo(EMAIL);
        assertThat(customer.getPhoneNumber()).isEqualTo(PHONE_NUMBER);
    }

}
