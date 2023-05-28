package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.ticket.TicketMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

public class TicketEndpointTest extends BaseIntegrationTest {

    private static final String TICKET_ENDPOINT = "/ticket";
    private static final String SPECIFIC_TICKET_PATH = TICKET_ENDPOINT + "/search/%s";
    private static final String CANCEL_TICKET_PATH = TICKET_ENDPOINT + "/cancel/ticket";
    private static final String COUNT_TICKET_PATH = TICKET_ENDPOINT + "/ticketCount/{ticketId}";
    private static final String RESERVE_TICKET_PATH = TICKET_ENDPOINT + "/reserve";
    private static final String BUY_TICKET_PATH = TICKET_ENDPOINT + "/buy";
    private static final String DELETE_TICKET_PATH = TICKET_ENDPOINT + "/delete";
    private static final String SAVE_TICKET_PATH = TICKET_ENDPOINT + "/save/all/%d";

    private static final String KEYWORD = "max";

    private static final Long PERFORMANCE_ID = 105L;
    private static final Long TICKET_ID = 1L;
    private static final Double TICKET_PRICE = 250d;
    private static final String TICKET_RESERVATIONNUMBER = "01234567";
    private static final LocalDateTime TICKET_DATEOFISSUE = LocalDateTime.now().minusMonths(1);
    private static final TicketStatus TICKET_STATUS = TicketStatus.T;

    private static final Customer CUSTOMER = Customer.builder()
        .id(1L)
        .firstname("Max")
        .surname("Mustermann")
        .birthday(LocalDate.now())
        .email("max.mustermann@mustermail.com")
        .address("Argentinierstrasse 8")
        .phoneNumber("066688888888")
        .build();

    private static final Integer COUNT_TICKET = 1;

    private Ticket ticket;
    private List<Ticket> ticketList;

    @Autowired
    private TicketMapper ticketMapper;

    @MockBean
    private TicketRepository ticketRepository;

    @MockBean
    private TicketSeatRepository ticketSeatRepository;

    @MockBean
    private SeatRepository seatRepository;

    @MockBean
    private InvoiceRepository invoiceRepository;

    @MockBean
    private PerformanceRepository performanceRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @Before
    public void setUp(){
        Performance performance = Performance.builder()
            .id(PERFORMANCE_ID)
            .artists(null)
            .event(null)
            .hall(null)
            .startDateTime(LocalDateTime.now().plusHours(10))
            .endDateTime(LocalDateTime.now().plusHours(12))
            .basePrice(40d)
            .leftCapacity(55)
            .build();

         ticket = Ticket.builder()
            .performance(performance)
            .reservationNumber(TICKET_RESERVATIONNUMBER)
            .price(TICKET_PRICE)
            .customer(CUSTOMER)
            .id(TICKET_ID)
            .dateOfIssue(TICKET_DATEOFISSUE)
            .status(TICKET_STATUS)
            .build();
         ticketList = new ArrayList<>();
         ticketList.add(ticket);

        BDDMockito.doNothing().when(performanceRepository).updateSeatCapacity(PERFORMANCE_ID);

        BDDMockito.doAnswer(inv -> inv.getArgument(0)).
            when(invoiceRepository).save(any(Invoice.class));

        BDDMockito.doAnswer(inv -> inv.getArgument(0)).
            when(ticketRepository).save(any(Ticket.class));
    }


    @Test
    public void findAllTicketUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(TICKET_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }


    @Test
    public void findAllTicketAsUser() {
        PageRequest request = PageRequest.of(1,10);
        BDDMockito.given(ticketRepository.findAllByStatusIsNot(TicketStatus.RC,request)).
            willReturn(new PageImpl<>(ticketList,request,ticketList.size()));

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(getServiceURI(TICKET_ENDPOINT, request))
            .then().extract().response();
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        List<TicketDTO> actual =  response.getBody().jsonPath().getList("entities",TicketDTO.class);
        List<TicketDTO> expected = ticketMapper.ticketToTicketDTO(ticketList);
        assertEquals(expected, actual);
    }

    @Test
    public void findTicketsByKeywordUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(String.format(SPECIFIC_TICKET_PATH, KEYWORD))
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findTicketsByKeywordAsUser() {
        PageRequest request = PageRequest.of(1,10);
        BDDMockito.given(ticketRepository.findByreservationNumberContainingOrCustomer_FirstnameOrCustomer_SurnameContainingAllIgnoreCase(KEYWORD,KEYWORD,KEYWORD,request)).
            willReturn(new PageImpl<>(ticketList,request,ticketList.size()));

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(getServiceURI(String.format(SPECIFIC_TICKET_PATH, KEYWORD), request))
            .then().extract().response();
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        List<TicketDTO> actual =  response.getBody().jsonPath().getList("entities",TicketDTO.class);
        List<TicketDTO> expected = ticketMapper.ticketToTicketDTO(ticketList);
        assertEquals(expected, actual);
    }

    @Test
    public void cancelTicketUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().put(CANCEL_TICKET_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void cancelInvalidTicketAsUser() {
        ticket.setStatus(TicketStatus.IC);
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(ticket)
            .when().put(CANCEL_TICKET_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_ACCEPTABLE.value()));
    }

    @Test
    public void cancelValidTicketAsUser() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(ticket)
            .when().put(CANCEL_TICKET_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        BDDMockito.verify(performanceRepository, Mockito.times(1)).updateSeatCapacity(PERFORMANCE_ID);
    }

    @Test
    public void saveTicketsToAnonymous(){
        Performance performance = ticket.getPerformance();
        List<Long> seatIds = List.of(1L,2L,3L);
        Map<Long,Seat> seatsMap = new HashMap<>();
        seatIds.forEach(id -> seatsMap.put(id, Seat.builder().id(id).type(SeatType.SEAT).multiplier(1.1).build()));
        List<Seat> seats = new ArrayList<>(seatsMap.values());

        BDDMockito.given(customerRepository.findOneById(1L))
            .willReturn(Optional.of(CUSTOMER));

        BDDMockito.given(performanceRepository.findOneById(performance.getId()))
            .willReturn(Optional.of(performance));

        BDDMockito.given(seatRepository.findAllById(seatIds))
            .willReturn(seats);

        BDDMockito.doAnswer(inv -> null)
            .when(seatRepository).isSeatAvailable(any(),any());

        BDDMockito.doAnswer(inv -> inv.getArgument(0))
            .when(ticketSeatRepository).save(any(TicketSeat.class));

        BDDMockito.doAnswer(inv -> inv.getArgument(0))
            .when(ticketRepository).save(any(Ticket.class));


        Double expectedPrice = 0d;
        for(Seat seat : seats){
            expectedPrice += seat.getMultiplier()*performance.getBasePrice();
        }
        expectedPrice = Math.round(expectedPrice*100.0)/100.0;


        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(seatIds)
            .when().post(String.format(SAVE_TICKET_PATH,performance.getId()))
            .then().extract().response();


        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        BDDMockito.verify(performanceRepository, Mockito.times(1)).findOneById(performance.getId());
        BDDMockito.verify(ticketRepository, Mockito.times(1)).save(any());
        BDDMockito.verify(ticketSeatRepository, Mockito.times(seatIds.size())).save(any());
        TicketDTO actualTicketDTO = response.as(TicketDTO.class);
        Assert.assertEquals(expectedPrice, actualTicketDTO.getPrice());
    }

    @Test
    public void countTicketSeatByTicketIdAsUser(){
        BDDMockito.
            given(ticketSeatRepository.countTicketSeatByTicketId(TICKET_ID)).
            willReturn(COUNT_TICKET);

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(COUNT_TICKET_PATH, TICKET_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(Integer.class), is(COUNT_TICKET));
    }

    @Test
    public void reserveTicketAsUser(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(ticketMapper.ticketToTicketDTO(ticket))
            .when().put(RESERVE_TICKET_PATH)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(TicketDTO.class).getStatus(), is(TicketStatus.R));
    }


    @Test
    public void reserveCanceledTicketAsUser(){
        ticket.setStatus(TicketStatus.RC);
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(ticketMapper.ticketToTicketDTO(ticket))
            .when().put(RESERVE_TICKET_PATH)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_ACCEPTABLE.value()));
    }


    @Test
    public void reserveTicketForStartedPerformanceAsUser(){
        ticket.getPerformance().setStartDateTime(LocalDateTime.now().minusHours(1));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(ticketMapper.ticketToTicketDTO(ticket))
            .when().put(RESERVE_TICKET_PATH)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void buyTicketForStartedPerformanceAsUser(){
        ticket.getPerformance().setStartDateTime(LocalDateTime.now().minusHours(1));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(ticketMapper.ticketToTicketDTO(ticket))
            .when().post(BUY_TICKET_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void buyValidTicketAsUser(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(ticketMapper.ticketToTicketDTO(ticket))
            .when().post(BUY_TICKET_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void deleteTicketAsUser(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(ticketMapper.ticketToTicketDTO(ticket))
            .when().delete(DELETE_TICKET_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        BDDMockito.verify(ticketRepository, Mockito.times(1)).delete(ticket);
    }
}
