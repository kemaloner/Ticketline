package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"invoicenumber"})})
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_invoice_id")
    @SequenceGenerator(name = "seq_invoice_id", sequenceName = "seq_invoice_id")
    private Long id;

    @Column(nullable = false)
    private String invoiceNumber;

    @Column(nullable = false)
    private LocalDateTime dateOfIssue;

    @ManyToOne(fetch = FetchType.EAGER)
    private Ticket ticket;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    @Override
    public String toString() {
        return "Invoice{" +
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
        Invoice invoice = (Invoice) o;
        return Objects.equals(id, invoice.id) &&
            Objects.equals(invoiceNumber, invoice.invoiceNumber) &&
            Objects.equals(dateOfIssue, invoice.dateOfIssue) &&
            Objects.equals(ticket, invoice.ticket);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, invoiceNumber, dateOfIssue, ticket);
    }

    public static InvoiceBuilder builder(){ return new InvoiceBuilder(); }

    public static final class InvoiceBuilder{

        private Long id;
        private String invoiceNumber;
        private LocalDateTime dateOfIssue;
        private Ticket ticket;

        public InvoiceBuilder id(Long id){
            this.id = id;
            return this;
        }

        public InvoiceBuilder invoiceNumber(String invoiceNumber){
            this.invoiceNumber = invoiceNumber;
            return this;
        }

        public InvoiceBuilder dateOfIssue(LocalDateTime dateOfIssue){
            this.dateOfIssue = dateOfIssue;
            return this;
        }

        public InvoiceBuilder ticket(Ticket ticket){
            this.ticket = ticket;
            return this;
        }

        public Invoice build(){
            Invoice invoice = new Invoice();
            invoice.setId(id);
            invoice.setInvoiceNumber(invoiceNumber);
            invoice.setDateOfIssue(dateOfIssue);
            invoice.setTicket(ticket);
            return invoice;
        }
    }

}
