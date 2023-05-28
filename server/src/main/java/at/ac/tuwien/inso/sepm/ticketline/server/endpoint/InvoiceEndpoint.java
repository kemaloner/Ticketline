package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.invoice.InvoiceDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.invoice.InvoiceMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.InvoiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/invoice")
@Api(value = "invoice")
public class InvoiceEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceEndpoint.class);


    private final InvoiceMapper invoiceMapper;
    private final InvoiceService invoiceService;

    InvoiceEndpoint(InvoiceMapper invoiceMapper, InvoiceService invoiceService){
        this.invoiceMapper = invoiceMapper;
        this.invoiceService = invoiceService;
    }

    @RequestMapping(value = "/findOneById/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get detailed information about a specific invoice entry")
    public InvoiceDTO findById(@PathVariable("id") Long id) {
        LOGGER.info("Loading invoice with id " + id);
        return invoiceMapper.invoiceToInvoiceDTO(invoiceService.findOneById(id));
    }

    @RequestMapping(value = "/findOneByTicketId/{ticket_id}", method = RequestMethod.GET)
    @ApiOperation(value = "Fetch the invoice fo a specific ticket")
    public InvoiceDTO findByTicketId(@PathVariable("ticket_id") Long ticket_id){
        LOGGER.info("Loading invoice with ticket id " + ticket_id);
        return invoiceMapper.invoiceToInvoiceDTO(invoiceService.findByTicketId(ticket_id));
    }
}
