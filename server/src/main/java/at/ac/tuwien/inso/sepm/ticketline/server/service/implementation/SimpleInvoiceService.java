package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Invoice;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.InvoiceRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.InvoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SimpleInvoiceService implements InvoiceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleInvoiceService.class);

    private final InvoiceRepository invoiceRepository;

    public SimpleInvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public Invoice findOneById(Long id) {
        LOGGER.info("Find an invoice by id " + id);
        return invoiceRepository.findOneByInvoiceNumber(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public Invoice findByTicketId(Long ticket_id) {
        LOGGER.info("Find an invoice by ticket id " + ticket_id);
        return invoiceRepository.findByTicketId(ticket_id).orElseThrow(NotFoundException::new);
    }

    @Override
    public Long countInvoiceByDateOfIssueYear() {
        LOGGER.info("Counting invoice by date of issue");
        return invoiceRepository.countInvoiceByDateOfIssueYear();
    }

    @Override
    public Invoice save(Invoice invoice) {
        LOGGER.info("Save an invoice");
        return invoiceRepository.save(invoice);
    }
}
