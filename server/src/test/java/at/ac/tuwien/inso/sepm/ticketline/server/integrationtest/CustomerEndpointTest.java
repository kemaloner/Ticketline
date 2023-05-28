package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.customer.CustomerMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

public class CustomerEndpointTest extends BaseIntegrationTest {

    private static final String CUSTOMER_ENDPOINT = "/customer";
    private static final String CUSTOMER_SEARCH = CUSTOMER_ENDPOINT + "/search/";
    private static final String CUSTOMER_SAVE = CUSTOMER_ENDPOINT + "/save";
    private static final String CUSTOMER_UPDATE = CUSTOMER_ENDPOINT + "/update";

    @MockBean
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    private Customer customer1, customer2;
    private List<Customer> customers;

    @Before
    public void setUp(){
        customer1 = Customer.builder()
            .firstname("John")
            .surname("Doe")
            .email("johndoe@ticketline.com")
            .address("Wien")
            .birthday(LocalDate.now().minusYears(30))
            .phoneNumber("004301010101")
            .build();

        customer2 = Customer.builder()
            .firstname("Jane")
            .surname("Doe")
            .email("jonedoe@ticketline.com")
            .address("Tirol")
            .birthday(LocalDate.now().minusYears(25))
            .phoneNumber("004301010102")
            .build();

        customers = Arrays.asList(customer1,customer2);
    }

    @Test
    public void findAllCustomersUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(CUSTOMER_ENDPOINT)
            .then().extract().response();
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());
    }

    @Test
    public void findAllCustomersAsUser() {
        PageRequest request = PageRequest.of(1,10);
        BDDMockito.given(customerRepository.findAll(request)).
            willReturn(new PageImpl<>(customers,request,customers.size()));

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(getServiceURI(CUSTOMER_ENDPOINT, request))
            .then().extract().response();
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        List<CustomerDTO> actual =  response.getBody().jsonPath().getList("entities",CustomerDTO.class);
        List<CustomerDTO> expected = customerMapper.customerToCustomerDTO(customers);
        assertEquals(expected, actual);
    }

    @Test
    public void findCustomersByKeywordAsUser() {
        String keyword = "adam";
        PageRequest request = PageRequest.of(1,10);
        BDDMockito.given(customerRepository.findByKeyword(keyword, request)).
            willReturn(new PageImpl<>(customers,request,customers.size()));

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(getServiceURI(CUSTOMER_SEARCH + keyword, request))
            .then().extract().response();
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        List<CustomerDTO> actual =  response.getBody().jsonPath().getList("entities",CustomerDTO.class);
        List<CustomerDTO> expected = customerMapper.customerToCustomerDTO(customers);
        assertEquals(expected, actual);
    }

    @Test
    public void saveCustomerAsUser() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(customerMapper.customerToCustomerDTO(customer1))
            .when().post(CUSTOMER_SAVE)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void updateCustomerAsUser() {
        BDDMockito.
            given(customerRepository.findById(customer1.getId())).
            willReturn(Optional.of(customer1));

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(customerMapper.customerToCustomerDTO(customer1))
            .when().put(CUSTOMER_UPDATE)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

}
