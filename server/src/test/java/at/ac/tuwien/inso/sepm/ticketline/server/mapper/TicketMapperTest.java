package at.ac.tuwien.inso.sepm.ticketline.server.mapper;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.customer.CustomerMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.performance.PerformanceMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.ticket.TicketMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class TicketMapperTest {

    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper")
    public static class TicketMapperTestContextConfiguration {
    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private TicketMapper ticketMapper;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    private CustomerMapper customerMapper;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    private PerformanceMapper performanceMapper;

    private static final Long TICKET_ID = 1L;
    private static final Double TICKET_PRICE = 250d;
    private static final String TICKET_RESERVATIONNUMBER = "01234567";
    private static final LocalDateTime TICKET_DATEOFISSUE = LocalDateTime.now();
    private static final TicketStatus TICKET_STATUS = TicketStatus.S;

    private static final Customer CUSTOMER = Customer.builder()
        .id(1L)
        .firstname("Max")
        .surname("Mustermann")
        .birthday(LocalDate.now())
        .email("max.mustermann@mustermail.com")
        .address("Argentinierstrasse 8")
        .phoneNumber("066688888888")
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

    @Test
    public void shouldMapTicketToTicketDTO(){
        Ticket ticket = Ticket.builder()
            .performance(PERFORMANCE)
            .reservationNumber(TICKET_RESERVATIONNUMBER)
            .price(TICKET_PRICE)
            .customer(CUSTOMER)
            .id(TICKET_ID)
            .dateOfIssue(TICKET_DATEOFISSUE)
            .status(TICKET_STATUS)
            .build();

        TicketDTO ticketDTO = ticketMapper.ticketToTicketDTO(ticket);

        assertThat(ticketDTO).isNotNull();
        assertThat(ticketDTO.getId()).isEqualTo(ticket.getId());
        assertThat(ticketDTO.getCustomer()).isEqualTo(customerMapper.customerToCustomerDTO(ticket.getCustomer()));
        assertThat(ticketDTO.getDateOfIssue()).isEqualTo(ticket.getDateOfIssue());
        assertThat(ticketDTO.getPerformance()).isEqualTo(performanceMapper.performanceToPerformanceDTO(ticket.getPerformance()));
        assertThat(ticketDTO.getPrice()).isEqualTo(ticket.getPrice());
        assertThat(ticketDTO.getReservationNumber()).isEqualTo(ticket.getReservationNumber());
        assertThat(ticketDTO.getStatus()).isEqualTo(ticket.getStatus());
    }

    @Test
    public void shouldMapTicketDTOToTicket(){
        TicketDTO ticketDTO = TicketDTO.builder()
            .id(TICKET_ID)
            .reservationNumber(TICKET_RESERVATIONNUMBER)
            .price(TICKET_PRICE)
            .performance(performanceMapper.performanceToPerformanceDTO(PERFORMANCE))
            .customer(customerMapper.customerToCustomerDTO(CUSTOMER))
            .dateOfIssue(TICKET_DATEOFISSUE)
            .status(TICKET_STATUS)
            .build();

        Ticket ticket = ticketMapper.ticketDTOToTicket(ticketDTO);

        assertThat(ticket).isNotNull();
        assertThat(ticket.getId()).isEqualTo(ticketDTO.getId());
        assertThat(ticket.getPrice()).isEqualTo(ticketDTO.getPrice());
        assertThat(ticket.getDateOfIssue()).isEqualTo(ticketDTO.getDateOfIssue());
        assertThat(ticket.getReservationNumber()).isEqualTo(ticketDTO.getReservationNumber());
        assertThat(ticket.getStatus()).isEqualTo(ticketDTO.getStatus());
        assertThat(ticket.getCustomer()).isEqualTo(customerMapper.customerDTOToCustomer(ticketDTO.getCustomer()));
        assertThat(ticket.getPerformance()).isEqualTo(performanceMapper.performanceDTOToPerformance(ticketDTO.getPerformance()));

    }
}
