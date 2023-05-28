package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "Customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_customer_id")
    @SequenceGenerator(name = "seq_customer_id", sequenceName = "seq_customer_id")
    private Long id;

    @Column(nullable = false, name = "firstname")
    @Size(max = 100)
    private String firstname;

    @Column(nullable = false, name = "surname")
    @Size(max = 100)
    private String surname;

    @Column(nullable = false, name = "birthday")
    private LocalDate birthday;

    @Column(name = "address")
    @Size(max = 100)
    private String address;

    @Column(name = "email")
    @Size(max = 100)
    private String email;

    @Column(name = "phoneNumber")
    @Size(max = 100)
    private String phoneNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long ID) {
        this.id = ID;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public static CustomerBuilder builder(){
        return new CustomerBuilder();
    }

    @Override
    public String toString(){
        return "News{" +
            "id=" + id +
            ", name=" + firstname +
            ", surname='" + surname + '\'' +
            ", birthday='" + birthday.toString()+ '\'' +
            ", adress='" + address +'\'' +
            ", email='" + email + '\'' +
            ", phone number='" + phoneNumber + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) &&
            Objects.equals(firstname, customer.firstname) &&
            Objects.equals(surname, customer.surname) &&
            Objects.equals(birthday, customer.birthday) &&
            Objects.equals(address, customer.address) &&
            Objects.equals(email, customer.email) &&
            Objects.equals(phoneNumber, customer.phoneNumber);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, firstname, surname, birthday, address, email, phoneNumber);
    }

    public static final class CustomerBuilder {
        private Long id;
        private String firstname;
        private String surname;
        private LocalDate birthday;
        private String address;
        private String email;
        private String phoneNumber;

        private CustomerBuilder() {
        }

        public Customer.CustomerBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public Customer.CustomerBuilder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public Customer.CustomerBuilder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public Customer.CustomerBuilder birthday(LocalDate birthday) {
            this.birthday = birthday;
            return this;
        }

        public Customer.CustomerBuilder address(String address){
            this.address = address;
            return this;
        }

        public Customer.CustomerBuilder email(String email){
            this.email = email;
            return this;
        }

        public Customer.CustomerBuilder phoneNumber(String phoneNumber){
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Customer build() {
            Customer customer = new Customer();
            customer.setId(id);
            customer.setFirstname(firstname);
            customer.setSurname(surname);
            customer.setBirthday(birthday);
            customer.setAddress(address);
            customer.setEmail(email);
            customer.setPhoneNumber(phoneNumber);
            return customer;
        }
    }
}
