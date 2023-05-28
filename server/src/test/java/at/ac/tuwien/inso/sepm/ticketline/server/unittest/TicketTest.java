package at.ac.tuwien.inso.sepm.ticketline.server.unittest;


import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.ServerServiceValidationException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.*;
import at.ac.tuwien.inso.sepm.ticketline.server.service.InvoiceService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ReservationNumberService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.TicketService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.implementation.SimpleTicketService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
public class TicketTest {
    private static final Long TICKET_ID1 = 1L;
    private static final Double TICKET_PRICE1 = 300.0;
    private static final String TICKET_RESNUMBER1 = "RESNUM_1";
    private static final Performance TICKET_PERFORMANCE1 = Performance.builder()
        .id(1L)
        .artists(null)
        .event(null)
        .hall(null)
        .startDateTime(LocalDateTime.of(2020,10,4,20,0))
        .endDateTime(LocalDateTime.of(2020,10,4,20,0))
        .basePrice(40d)
        .leftCapacity(55)
        .build();
    private static final Customer TICKET_CUSTOMER1 = Customer.builder()
        .id(1L)
        .firstname("Max")
        .surname("Mustermann")
        .birthday(LocalDate.now())
        .email("max.mustermann@mustermail.com")
        .address("Argentinierstrasse 8")
        .phoneNumber("066688888888")
        .build();
    private static final TicketStatus TICKET_STATUS1 = null;
    private static final LocalDateTime TICKET_DATE1 = LocalDateTime.of(2018,4,4,20,0);

    private static final Long TICKET_ID2 = 2L;
    private static final Double TICKET_PRICE2 = 600.0;
    private static final String TICKET_RESNUMBER2 = "RESNUM_2";
    private static final Performance TICKET_PERFORMANCE2 = Performance.builder()
        .id(2L)
        .artists(null)
        .event(null)
        .hall(null)
        .startDateTime(LocalDateTime.now())
        .endDateTime(LocalDateTime.now())
        .basePrice(40d)
        .leftCapacity(55)
        .build();
    private static final Customer TICKET_CUSTOMER2 = Customer.builder()
        .id(2L)
        .firstname("Max")
        .surname("Mustermann")
        .birthday(LocalDate.now())
        .email("max.mustermann@mustermail.com")
        .address("Argentinierstrasse 8")
        .phoneNumber("066688888888")
        .build();
    private static final TicketStatus TICKET_STATUS2 = null;
    private static final LocalDateTime TICKET_DATE2 = LocalDateTime.of(2018,8,4,20,0);

    @MockBean
    private TicketRepository ticketRepository;

    @MockBean
    private TicketSeatRepository ticketSeatRepository;

    @MockBean
    private PerformanceRepository performanceRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private SeatRepository seatRepository;

    @MockBean
    private InvoiceService invoiceService;

    @MockBean
    private ReservationNumberService reservationNumberService;

    //@Autowired
    private TicketService ticketService;

    private List<Ticket> ticketList;
    private Ticket ticket1, ticket2;
    private Map<Long, Ticket> ticketsMap;

    @Before
    public void setUp(){


        ticketService = new SimpleTicketService(ticketRepository,ticketSeatRepository,customerRepository,seatRepository,performanceRepository,reservationNumberService,invoiceService);
        ticket1 = Ticket.builder()
            .id(TICKET_ID1)
            .performance(TICKET_PERFORMANCE1)
            .customer(TICKET_CUSTOMER1)
            .status(TICKET_STATUS1)
            .price(TICKET_PRICE1)
            .dateOfIssue(TICKET_DATE1)
            .reservationNumber(TICKET_RESNUMBER1)
            .build();

        ticket2 = Ticket.builder()
            .id(TICKET_ID2)
            .performance(TICKET_PERFORMANCE2)
            .customer(TICKET_CUSTOMER2)
            .status(TICKET_STATUS2)
            .price(TICKET_PRICE2)
            .dateOfIssue(TICKET_DATE2)
            .reservationNumber(TICKET_RESNUMBER2)
            .build();

        ticketList = new ArrayList<>();
        ticketList.add(ticket1);
        ticketList.add(ticket2);

        ticketsMap = new HashMap<>();
        ticketsMap.put(TICKET_ID1, ticket1);
        ticketsMap.put(TICKET_ID2, ticket2);

        BDDMockito.doAnswer(inv -> {
            Ticket saveTickets = inv.getArgument(0);
            if (saveTickets.getId() == null){
                saveTickets.setId(1L + ticketsMap.size());
            }
            ticketsMap.put(saveTickets.getId(), saveTickets);
            return saveTickets;
        }).when(ticketRepository).save(any(Ticket.class));

    }


    @Test
    public void findAllTickets(){
        PageRequest request = PageRequest.of(1,10);
        BDDMockito.
            given(ticketRepository.findAllByStatusIsNot(TicketStatus.RC, request)).
            willReturn(new PageImpl<Ticket>(ticketList,request,ticketList.size()));

        Page<Ticket> page = ticketService.findAll(request);

        assertEquals(ticketList, page.getContent());
    }

    @Test
    public void findOneTicket(){
        Long ticketId = 1L;
        BDDMockito.
            given(ticketRepository.findById(ticketId)).
            willReturn(Optional.of(ticket1));

        Ticket actualTicket = ticketService.findOne(ticketId);

        assertThat(ticket1).isEqualTo(actualTicket);
    }

    @Test
    public void findTicketsByKeyword(){
        PageRequest request = PageRequest.of(1,10);
        BDDMockito.
            given(ticketRepository.findByreservationNumberContainingOrCustomer_FirstnameOrCustomer_SurnameContainingAllIgnoreCase(ticket1.getReservationNumber(),ticket1.getReservationNumber(),ticket1.getReservationNumber(),request)).
            willReturn(new PageImpl<>(ticketList,request,ticketList.size()));

        Page<Ticket> page = ticketService.findByKeyword(ticket1.getReservationNumber(),request);

        assertEquals(ticketList, page.getContent());
    }

    @Test
    public void saveTicket(){

        Customer customer = new Customer();
        customer.setId(4L);
        customer.setFirstname("CUSTOMER_4");

        Performance performance = new Performance();
        performance.setId(4L);
        Event event = new Event();
        event.setTitle("EVENT_4");
        performance.setEvent(event);

        Ticket ticketExpected = Ticket.builder()
            .id(4L)
            .reservationNumber("RESNUM_4")
            .price(200.0)
            .status(null)
            .dateOfIssue(TICKET_DATE2)
            .customer(customer)
            .performance(performance)
            .build();

        Ticket ticketActual = ticketService.saveTicket(ticketExpected);

        assertThat(ticketExpected).isEqualTo(ticketActual);
    }

    @Test
    public void reserveTicket(){

        Ticket ticketExpected = ticket1;
        Ticket ticketActual = ticketService.reserve(ticketExpected);

        assertThat(TicketStatus.R).isEqualTo(ticketActual.getStatus());
    }

    @Test
    public void buyTicket(){

        Ticket ticketExpected = ticket1;
        Ticket ticketActual = ticketService.buy(ticketExpected);

        assertThat(TicketStatus.S).isEqualTo(ticketActual.getStatus());
    }


    @Test(expected = ServerServiceValidationException.class)
    public void buyCanceledTicket(){

        Ticket ticketExpected = ticket1;
        ticketExpected.setStatus(TicketStatus.IC);

        Ticket ticketActual = ticketService.buy(ticketExpected);

        assertThat(ticketExpected).isEqualTo(ticketActual);
    }

    @Test
    public void cancelTicket(){

        Ticket ticketActual = ticketService.cancelTicket(ticket1);
        ticket1.setStatus(TicketStatus.IC);
        assertThat(ticket1).isEqualTo(ticketActual);
    }

    @Test(expected = ServerServiceValidationException.class)
    public void CancelCanceledTicket(){

        Ticket ticketExpected = ticket2;
        ticketExpected.setStatus(TicketStatus.IC);

        Ticket ticketActual = ticketService.cancelTicket(ticketExpected);
        assertThat(ticketExpected).isEqualTo(ticketActual);
    }

}
