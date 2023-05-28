package at.ac.tuwien.inso.sepm.ticketline.rest.location;

import at.ac.tuwien.inso.sepm.ticketline.rest.hall.HallDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "LocationDTO", description = "A detailed DTO for location entries via rest")
public class LocationDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(required = true, readOnly = true, name = "The description of the location")
    private String description;

    @ApiModelProperty(required = true, readOnly = true, name = "The country of the location")
    private String country;

    @ApiModelProperty(required = true, readOnly = true, name = "The city of the location")
    private String city;

    @ApiModelProperty(required = true, readOnly = true, name = "The zip of the location")
    private String zip;

    @ApiModelProperty(required = true, readOnly = true, name = "The street of the location")
    private String street;

    @ApiModelProperty(required = true, readOnly = true, name = "The houseNumber of the location")
    private Integer houseNumber;

    @ApiModelProperty(required = true, readOnly = true, name = "The list of halls of the location")
    private List<HallDTO> halls;

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

    public List<HallDTO> getHalls() {
        return halls;
    }

    public void setHalls(List<HallDTO> halls) {
        this.halls = halls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocationDTO that = (LocationDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (country != null ? !country.equals(that.country) : that.country != null) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (zip != null ? !zip.equals(that.zip) : that.zip != null) return false;
        if (street != null ? !street.equals(that.street) : that.street != null) return false;
        if (houseNumber != null ? !houseNumber.equals(that.houseNumber) : that.houseNumber != null) return false;
        return halls != null ? halls.equals(that.halls) : that.halls == null;
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
        return "LocationDTO{" +
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

    public static LocationDTOBuilder builder(){
        return new LocationDTOBuilder();
    }

    public static final class LocationDTOBuilder{

        private Long id;
        private String description;
        private String country;
        private String city;
        private String zip;
        private String street;
        private Integer houseNumber;
        private List<HallDTO> halls;

        public LocationDTOBuilder id(Long id){
            this.id = id;
            return this;
        }

        public LocationDTOBuilder description(String description){
            this.description = description;
            return this;
        }

        public LocationDTOBuilder country(String country){
            this.country = country;
            return this;
        }

        public LocationDTOBuilder city(String city){
            this.city = city;
            return this;
        }

        public LocationDTOBuilder zip(String zip){
            this.zip = zip;
            return this;
        }

        public LocationDTOBuilder street(String street){
            this.street = street;
            return this;
        }

        public LocationDTOBuilder houseNumber(Integer houseNumber){
            this.houseNumber = houseNumber;
            return this;
        }

        public LocationDTOBuilder halls(List<HallDTO> halls){
            this.halls = halls;
            return this;
        }

        public LocationDTO builder(){
            LocationDTO locationDTO = new LocationDTO();
            locationDTO.setId(id);
            locationDTO.setCity(city);
            locationDTO.setCountry(country);
            locationDTO.setDescription(description);
            locationDTO.setHalls(halls);
            locationDTO.setHouseNumber(houseNumber);
            locationDTO.setStreet(street);
            locationDTO.setZip(zip);
            return locationDTO;
        }
    }
}
