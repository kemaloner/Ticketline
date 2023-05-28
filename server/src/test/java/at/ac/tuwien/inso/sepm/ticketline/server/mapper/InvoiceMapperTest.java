package at.ac.tuwien.inso.sepm.ticketline.server.mapper;

import at.ac.tuwien.inso.sepm.ticketline.rest.invoice.InvoiceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Invoice;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.invoice.InvoiceMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.ticket.TicketMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RunWith(SpringJUnit4ClassRunner.class)
public class InvoiceMapperTest {

    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper")
    public static class InvoiceMapperTestContextConfiguration {
    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private InvoiceMapper invoiceMapper;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private TicketMapper ticketMapper;

    private static final Long INVOICE_INVOICENUMBER = 1L;
    private static final LocalDateTime INVOICE_DATEOFISSUE = LocalDateTime.of(2018, 6, 4, 0, 0);
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
        .id(1L)
        .customer(CUSTOMER)
        .price(250d)
        .reservationNumber("SOME RANDOM RES NO")
        .performance(PERFORMANCE)
        .build();

    @Test
    public void shouldMapInvoiceToInvoiceDTO(){

        Invoice invoice = Invoice.builder()
            .invoiceNumber(String.valueOf(INVOICE_INVOICENUMBER))
            .dateOfIssue(INVOICE_DATEOFISSUE)
            .ticket(TICKET)
            .build();

        InvoiceDTO invoiceDTO = invoiceMapper.invoiceToInvoiceDTO(invoice);
        assertThat(invoiceDTO).isNotNull();
        assertThat(invoice.getInvoiceNumber()).isEqualTo(invoiceDTO.getInvoiceNumber());
        assertThat(invoice.getDateOfIssue()).isEqualTo(invoiceDTO.getDateOfIssue());
        assertThat(invoice.getTicket()).isEqualTo(ticketMapper.ticketDTOToTicket(invoiceDTO.getTicket()));
    }

    @Test
    public void shouldMapInvoiceDTOToInvoice(){

        TicketDTO ticketDTO = ticketMapper.ticketToTicketDTO(TICKET);

        InvoiceDTO invoiceDTO = InvoiceDTO.builder()
            .invoiceNumber(String.valueOf(INVOICE_INVOICENUMBER))
            .dateOfIssue(INVOICE_DATEOFISSUE)
            .ticket(ticketDTO)
            .build();

        Invoice invoice = invoiceMapper.invoiceDTOToInvoice(invoiceDTO);

        assertThat(invoice).isNotNull();
        assertThat(invoice.getInvoiceNumber()).isEqualTo(invoiceDTO.getInvoiceNumber());
        assertThat(invoice.getDateOfIssue()).isEqualTo(invoiceDTO.getDateOfIssue());
        assertThat(invoice.getTicket()).isEqualTo(TICKET);
    }

}
