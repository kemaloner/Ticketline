package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.invoice;

import at.ac.tuwien.inso.sepm.ticketline.rest.invoice.InvoiceDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Invoice;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

    Invoice invoiceDTOToInvoice(InvoiceDTO invoiceDTO);

    InvoiceDTO invoiceToInvoiceDTO(Invoice invoice);

    List<InvoiceDTO> invoiceToInvoiceDTO(List<Invoice> invoices);
}
