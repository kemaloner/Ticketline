package at.ac.tuwien.inso.sepm.ticketline.server.unittest;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Invoice;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.InvoiceRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.InvoiceService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.implementation.SimpleInvoiceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringJUnit4ClassRunner.class)
public class InvoiceTest {

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

    @MockBean
    private InvoiceRepository invoiceRepository;
    private InvoiceService invoiceService;

    private Invoice invoice;
    private Map<Long, Invoice> invoiceMap;

    @Before
    public void setUp(){
        invoiceService = new SimpleInvoiceService(invoiceRepository);
        invoice = Invoice.builder()
            .id(1L)
            .invoiceNumber(String.valueOf(INVOICE_INVOICENUMBER))
            .dateOfIssue(INVOICE_DATEOFISSUE)
            .ticket(TICKET)
            .build();

        invoiceMap = new HashMap<>();
        invoiceMap.put(INVOICE_INVOICENUMBER, invoice);

        BDDMockito.doAnswer(inv -> {
            Invoice invoice1 = inv.getArgument(0);
            if (invoice1.getId() == null){
                invoice1.setId(1L + invoiceMap.size());
            }
            invoiceMap.put(invoice1.getId(),invoice1);
            return invoice1;
        }).when(invoiceRepository).save(any(Invoice.class));
    }

    @Test
    public void findOneInvoice(){
        Long id = 1L;
        BDDMockito.
            given(invoiceRepository.findOneByInvoiceNumber(id)).
            willReturn(Optional.of(invoice));

        Invoice actualInvoice = invoiceService.findOneById(id);

        assertEquals(invoice, actualInvoice);
    }

    @Test
    public void findOneInvoiceByTicketId(){
        Long ticketId = 1L;
        BDDMockito.
            given(invoiceRepository.findByTicketId(ticketId)).
            willReturn(Optional.of(invoice));

        Invoice actualInvoice = invoiceService.findByTicketId(ticketId);

        assertEquals(invoice, actualInvoice);
    }

    @Test
    public void countInvoiceByDateOfIssueYear(){
        Long count = invoiceMap.size() + 0L;

        BDDMockito.
            given(invoiceRepository.countInvoiceByDateOfIssueYear()).
            willReturn(count);

        Long actualCount = invoiceService.countInvoiceByDateOfIssueYear();

        assertEquals(count, actualCount);
    }

    @Test
    public void saveInvoice(){
        Invoice invoiceSave = Invoice.builder()
            .id(1L)
            .invoiceNumber(String.valueOf(2L))
            .dateOfIssue(INVOICE_DATEOFISSUE)
            .ticket(TICKET)
            .build();

        Invoice isSaved = invoiceService.save(invoiceSave);

        assertEquals(String.valueOf(2L), isSaved.getInvoiceNumber());
    }
}
