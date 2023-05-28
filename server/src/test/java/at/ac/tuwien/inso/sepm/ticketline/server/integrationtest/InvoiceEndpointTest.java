package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.rest.invoice.InvoiceDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Invoice;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.invoice.InvoiceMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.InvoiceRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.core.Is.is;

public class InvoiceEndpointTest extends BaseIntegrationTest {

    @Autowired
    private InvoiceMapper invoiceMapper;

    @MockBean
    private InvoiceRepository invoiceRepository;

    private static final String INVOICE_ENDPOINT = "/invoice";
    private static final String SPECIFIC_INVOICE_PATH = INVOICE_ENDPOINT + "/findOneById/{id}";
    private static final String SPECIFIC_INVOICE_TICKET_PATH = INVOICE_ENDPOINT + "/findOneByTicketId/{ticket_id}";


    private static final Long INVOICE_ID = 1L;
    private static final Long INVOICE_INVOICENUMBER = 1L;
    private static final LocalDateTime INVOICE_DATEOFISSUE = LocalDateTime.of(2018, 6, 4, 0, 0);
    private static final Long TICKET_ID = 1L;
    private static final Customer CUSTOMER = Customer.builder()
        .id(1L)
        .address("Wiedner Hauptstrasse 76")
        .birthday(LocalDate.now())
        .email("e123@student.tuwien.ac.at")
        .firstname("Arda")
        .surname("Dereli")
        .phoneNumber("066188888888")
        .build();

    private static final Performance PERFORMANCE = Performance.builder()
        .id(1L)
        .artists(null)
        .event(null)
        .hall(null)
        .startDateTime(LocalDateTime.now())
        .endDateTime(LocalDateTime.now())
        .basePrice(40d)
        .leftCapacity(55)
        .build();
    private static final Ticket TICKET = Ticket.builder()
        .id(TICKET_ID)
        .customer(CUSTOMER)
        .price(250d)
        .reservationNumber("SOME RANDOM RES NO")
        .performance(PERFORMANCE)
        .build();

    private Invoice invoice;


    @Before
    public void setUp(){
        invoice = Invoice.builder()
            .id(INVOICE_ID)
            .invoiceNumber(String.valueOf(INVOICE_INVOICENUMBER))
            .dateOfIssue(INVOICE_DATEOFISSUE)
            .ticket(TICKET)
            .build();
    }

    @Test
    public void findInvoicesByInvoiceIdUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(SPECIFIC_INVOICE_PATH, INVOICE_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findInvoicesByInvoiceIdAsUser() {
        BDDMockito.
            given(invoiceRepository.findOneByInvoiceNumber(INVOICE_ID)).
            willReturn(Optional.of(invoice));

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(SPECIFIC_INVOICE_PATH, INVOICE_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(InvoiceDTO.class), is(invoiceMapper.invoiceToInvoiceDTO(invoice)));
    }

    @Test
    public void findInvoicesByTicketIdUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(SPECIFIC_INVOICE_TICKET_PATH, TICKET_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findInvoicesByTicketIdAsUser() {
        BDDMockito.
            given(invoiceRepository.findByTicketId(TICKET_ID)).
            willReturn(Optional.of(invoice));

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(SPECIFIC_INVOICE_TICKET_PATH, TICKET_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(InvoiceDTO.class), is(invoiceMapper.invoiceToInvoiceDTO(invoice)));
    }
}
