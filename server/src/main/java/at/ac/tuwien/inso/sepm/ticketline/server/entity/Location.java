package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_location_id")
    @SequenceGenerator(name = "seq_location_id", sequenceName = "seq_location_id")
    private Long id;

    @Column(nullable = false, name = "description")
    private String description;

    @Column(nullable = false, name = "country")
    private String country;

    @Column(nullable = false, name = "city")
    private String city;

    @Column(nullable = false, name = "zip")
    private String zip;

    @Column(nullable = false, name = "street")
    private String street;

    @Column(nullable = false, name = "houseNumber")
    private Integer houseNumber;

    @Transient
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<Hall> halls = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(Integer houseNumber) {
        this.houseNumber = houseNumber;
    }

    public List<Hall> getHalls() {
        return halls;
    }

    public void setHalls(List<Hall> halls) {
        this.halls = halls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (id != null ? !id.equals(location.id) : location.id != null) return false;
        if (description != null ? !description.equals(location.description) : location.description != null)
            return false;
        if (country != null ? !country.equals(location.country) : location.country != null) return false;
        if (city != null ? !city.equals(location.city) : location.city != null) return false;
        if (zip != null ? !zip.equals(location.zip) : location.zip != null) return false;
        if (street != null ? !street.equals(location.street) : location.street != null) return false;
        if (houseNumber != null ? !houseNumber.equals(location.houseNumber) : location.houseNumber != null)
            return false;
        return halls != null ? halls.equals(location.halls) : location.halls == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (zip != null ? zip.hashCode() : 0);
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (houseNumber != null ? houseNumber.hashCode() : 0);
        result = 31 * result + (halls != null ? halls.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Location{" +
            "id=" + id +
            ", description='" + description + '\'' +
            ", country='" + country + '\'' +
            ", city='" + city + '\'' +
            ", zip='" + zip + '\'' +
            ", street='" + street + '\'' +
            ", houseNumber=" + houseNumber +
            ", halls=" + halls +
            '}';
    }

    public static LocationBuilder builder(){
        return new LocationBuilder();
    }

    public static final class LocationBuilder{

        private Long id;
        private String description;
        private String country;
        private String city;
        private String zip;
        private String street;
        private Integer houseNumber;
        private List<Hall> halls;

        private LocationBuilder(){
        }

        public LocationBuilder id(Long id){
            this.id = id;
            return this;
        }

        public LocationBuilder description(String description){
            this.description = description;
            return this;
        }

        public LocationBuilder country(String country){
            this.country = country;
            return this;
        }

        public LocationBuilder city(String city){
            this.city = city;
            return this;
        }

        public LocationBuilder zip(String zip){
            this.zip = zip;
            return this;
        }

        public LocationBuilder street(String street){
            this.street = street;
            return this;
        }

        public LocationBuilder houseNumber(Integer houseNumber){
            this.houseNumber = houseNumber;
            return this;
        }

        public LocationBuilder halls(List<Hall> halls){
            this.halls = halls;
            return this;
        }

        public Location build(){
            Location location = new Location();
            location.setId(id);
            location.setDescription(description);
            location.setCountry(country);
            location.setCity(city);
            location.setZip(zip);
            location.setStreet(street);
            location.setHouseNumber(houseNumber);
            location.setHalls(halls);
            return location;
        }

    }
}
