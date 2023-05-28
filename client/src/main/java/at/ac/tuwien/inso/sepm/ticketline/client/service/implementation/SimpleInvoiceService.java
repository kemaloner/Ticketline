package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.InvoiceRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.TicketSeatRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.InvoiceService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.PdfUtil;
import at.ac.tuwien.inso.sepm.ticketline.rest.invoice.InvoiceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticketseat.TicketSeatDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;

@Service
public class SimpleInvoiceService implements InvoiceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleInvoiceService.class);

    private final InvoiceRestClient invoiceRestClient;
    private final TicketSeatRestClient ticketSeatRestClient;
    private final PdfUtil pdfUtil;

    public SimpleInvoiceService(InvoiceRestClient invoiceRestClient, TicketSeatRestClient ticketSeatRestClient, PdfUtil pdfUtil) {
        this.invoiceRestClient = invoiceRestClient;
        this.ticketSeatRestClient = ticketSeatRestClient;
        this.pdfUtil = pdfUtil;
    }


    @Override
    public String findOneById(Long id) throws DataAccessException, FileNotFoundException {
        LOGGER.info("Find an invoice by id " + id);
        InvoiceDTO invoiceDTO = invoiceRestClient.findOneById(id);
        List<TicketSeatDTO> ticketSeatDTOList = ticketSeatRestClient.findByTicketId(invoiceDTO.getTicket().getId());

        return pdfUtil.createPDF(invoiceRestClient.findOneById(id), ticketSeatDTOList);
    }

    @Override
    public String findByTicketId(Long ticket_id) throws DataAccessException, FileNotFoundException {
        LOGGER.info("Find an invoice by ticket id " + ticket_id);
        InvoiceDTO invoiceDTO = invoiceRestClient.findByTicketId(ticket_id);
        List<TicketSeatDTO> ticketSeatDTOList = ticketSeatRestClient.findByTicketId(ticket_id);

        return pdfUtil.createPDF(invoiceDTO, ticketSeatDTOList);
    }
}
