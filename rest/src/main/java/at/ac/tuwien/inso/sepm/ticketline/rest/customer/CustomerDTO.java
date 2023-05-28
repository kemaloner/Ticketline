package at.ac.tuwien.inso.sepm.ticketline.rest.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;
import java.util.Objects;

@ApiModel(value = "CustomerDTO", description = "DTO for customer entries")
public class CustomerDTO {

    @ApiModelProperty(readOnly = true, name = "id of the Customer")
    private Long id;

    @ApiModelProperty(required = true, name = "name of the Customer")
    private String firstname;

    @ApiModelProperty(required = true, name = "surname of the Customer")
    private String surname;

    @ApiModelProperty(required = true, name = "birthday of the Customer")
    private LocalDate birthday;

    @ApiModelProperty(name = "address of the Customer")
    private String address;

    @ApiModelProperty(name = "email of the Customer")
    private String email;

    @ApiModelProperty(name = "phone number of the customer")
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

    public static CustomerDTOBuilder builder(){ return new CustomerDTOBuilder(); }

    @Override
    public String toString(){
        return "Customer{" +
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
        CustomerDTO customer = (CustomerDTO) o;
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

    public static final class CustomerDTOBuilder {
        private Long id;
        private String firstname;
        private String surname;
        private LocalDate birthday;
        private String address;
        private String email;
        private String phoneNumber;

        public CustomerDTOBuilder() {
        }

        public CustomerDTO.CustomerDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CustomerDTO.CustomerDTOBuilder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public CustomerDTO.CustomerDTOBuilder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public CustomerDTO.CustomerDTOBuilder birthday(LocalDate birthday) {
            this.birthday = birthday;
            return this;
        }

        public CustomerDTO.CustomerDTOBuilder address(String address){
            this.address = address;
            return this;
        }

        public CustomerDTO.CustomerDTOBuilder email(String email){
            this.email = email;
            return this;
        }

        public CustomerDTO.CustomerDTOBuilder phoneNumber(String phoneNumber){
            this.phoneNumber = phoneNumber;
            return this;
        }

        public CustomerDTO build() {
            CustomerDTO customer = new CustomerDTO();
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
