package at.ac.tuwien.inso.sepm.ticketline.rest.invoice;

import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.Objects;

@ApiModel(value = "InvoiceDTO", description = "A simple DTO for invoice entries via rest")
public class InvoiceDTO {

    @ApiModelProperty(required = true, readOnly = true, name = "automatically generated id value")
    private Long id;

    @ApiModelProperty(required = true, readOnly = true, name = "invoice number")
    private String invoiceNumber;

    @ApiModelProperty(required = true, name = "date this invoice was created")
    private LocalDateTime dateOfIssue;

    @ApiModelProperty(required = true, name = "Ticket to which this invoice belongs to")
    private TicketDTO ticket;

    @Override
    public String toString() {
        return "InvoiceDTO{" +
            "id=" + id +
            ", invoiceNumber='" + invoiceNumber + '\'' +
            ", dateOfIssue=" + dateOfIssue +
            ", ticket=" + ticket +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceDTO that = (InvoiceDTO) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(invoiceNumber, that.invoiceNumber) &&
            Objects.equals(dateOfIssue, that.dateOfIssue) &&
            Objects.equals(ticket, that.ticket);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, invoiceNumber, dateOfIssue, ticket);
    }

    public String getInvoiceNumber() {

        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDateTime getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(LocalDateTime dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public TicketDTO getTicket() {
        return ticket;
    }

    public void setTicket(TicketDTO ticketDTO) {
        this.ticket = ticketDTO;
    }

    public static InvoiceDTOBuilder builder(){ return new InvoiceDTOBuilder(); }

    public static final class InvoiceDTOBuilder{

        private Long id;
        private String invoiceNumber;
        private LocalDateTime dateOfIssue;
        private TicketDTO ticket;

        public InvoiceDTOBuilder id(Long id){
            this.id = id;
            return this;
        }

        public InvoiceDTOBuilder invoiceNumber(String invoiceNumber){
            this.invoiceNumber = invoiceNumber;
            return this;
        }

        public InvoiceDTOBuilder dateOfIssue(LocalDateTime dateOfIssue){
            this.dateOfIssue = dateOfIssue;
            return this;
        }

        public InvoiceDTOBuilder ticket(TicketDTO ticket){
            this.ticket = ticket;
            return this;
        }

        public InvoiceDTO build(){
            InvoiceDTO invoiceDTO = new InvoiceDTO();
            invoiceDTO.setId(id);
            invoiceDTO.setInvoiceNumber(invoiceNumber);
            invoiceDTO.setDateOfIssue(dateOfIssue);
            invoiceDTO.setTicket(ticket);
            return invoiceDTO;
        }
    }
}
