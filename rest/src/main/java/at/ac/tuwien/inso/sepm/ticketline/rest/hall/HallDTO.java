package at.ac.tuwien.inso.sepm.ticketline.rest.hall;

import at.ac.tuwien.inso.sepm.ticketline.rest.location.LocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "HallDTO", description = "A detailed DTO for hall entries via rest")
public class HallDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(required = true, readOnly = true, name = "The description of the hall")
    private String description;

    @ApiModelProperty(required = true, readOnly = true, name = "The type of the hall")
    private String type;

    @ApiModelProperty(required = true, readOnly = true, name = "The capacity of the hall")
    private Integer capacity;

    @ApiModelProperty(required = true, readOnly = true, name = "The location of the hall")
    private LocationDTO location;

    @ApiModelProperty(required = true, readOnly = true, name = "The list of seats of the hall")
    private List<SeatDTO> seats;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    public List<SeatDTO> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatDTO> seats) {
        this.seats = seats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HallDTO hallDTO = (HallDTO) o;

        if (id != null ? !id.equals(hallDTO.id) : hallDTO.id != null) return false;
        if (description != null ? !description.equals(hallDTO.description) : hallDTO.description != null) return false;
        if (type != null ? !type.equals(hallDTO.type) : hallDTO.type != null) return false;
        if (capacity != null ? !capacity.equals(hallDTO.capacity) : hallDTO.capacity != null) return false;
        if (location != null ? !location.equals(hallDTO.location) : hallDTO.location != null) return false;
        return seats != null ? seats.equals(hallDTO.seats) : hallDTO.seats == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (capacity != null ? capacity.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (seats != null ? seats.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HallDTO{" +
            "id=" + id +
            ", description='" + description + '\'' +
            ", type='" + type + '\'' +
            ", capacity=" + capacity +
            ", location=" + location +
            ", seats=" + seats +
            '}';
    }

    public static HallDTOBuilder builder(){
        return new HallDTOBuilder();
    }

    public static final class HallDTOBuilder{

        private Long id;
        private String description;
        private String type;
        private Integer capacity;
        private LocationDTO location;
        private List<SeatDTO> seats;

        public HallDTOBuilder id(Long id){
            this.id = id;
            return this;
        }

        public HallDTOBuilder description(String description){
            this.description = description;
            return this;
        }

        public HallDTOBuilder type(String type){
            this.type = type;
            return this;
        }

        public HallDTOBuilder capacity(Integer capacity){
            this.capacity = capacity;
            return this;
        }

        public HallDTOBuilder location(LocationDTO location){
            this.location = location;
            return this;
        }

        public HallDTOBuilder seats(List<SeatDTO> seats){
            this.seats = seats;
            return this;
        }

        public HallDTO builder(){
            HallDTO hallDTO = new HallDTO();
            hallDTO.setId(id);
            hallDTO.setDescription(description);
            hallDTO.setType(type);
            hallDTO.setCapacity(capacity);
            hallDTO.setLocation(location);
            hallDTO.setSeats(seats);
            return hallDTO;
        }
    }
}
