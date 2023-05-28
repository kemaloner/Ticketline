package at.ac.tuwien.inso.sepm.ticketline.rest.performance;

import at.ac.tuwien.inso.sepm.ticketline.rest.artist.ArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.hall.HallDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "PerformanceDTO", description = "A simple DTO for performance entries via rest")
public class PerformanceDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(required = true, readOnly = true, name = "The start time of the performance")
    private LocalDateTime startDateTime;

    @ApiModelProperty(required = true, readOnly = true, name = "The end time of the performance")
    private LocalDateTime endDateTime;

    @ApiModelProperty(required = true, readOnly = true, name = "The base price of the performance")
    private Double basePrice;

    @ApiModelProperty(required = true, readOnly = true, name = "The left capacity of the performance")
    private Integer leftCapacity;

    @ApiModelProperty(required = true, readOnly = true, name = "The event of the performance")
    private EventDTO event;

    @ApiModelProperty(required = true, readOnly = true, name = "The list of artist of the performance")
    private List<ArtistDTO> artists;

    @ApiModelProperty(required = true, readOnly = true, name = "The hall of the performance")
    private HallDTO hall;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Integer getLeftCapacity() {
        return leftCapacity;
    }

    public void setLeftCapacity(Integer leftCapacity) {
        this.leftCapacity = leftCapacity;
    }

    public EventDTO getEvent() {
        return event;
    }

    public void setEvent(EventDTO event) {
        this.event = event;
    }

    public List<ArtistDTO> getArtists() {
        return artists;
    }

    public void setArtists(List<ArtistDTO> artists) {
        this.artists = artists;
    }

    public HallDTO getHall() {
        return hall;
    }

    public void setHall(HallDTO hall) {
        this.hall = hall;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PerformanceDTO that = (PerformanceDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (startDateTime != null ? !startDateTime.equals(that.startDateTime) : that.startDateTime != null)
            return false;
        if (endDateTime != null ? !endDateTime.equals(that.endDateTime) : that.endDateTime != null) return false;
        if (basePrice != null ? !basePrice.equals(that.basePrice) : that.basePrice != null) return false;
        if (leftCapacity != null ? !leftCapacity.equals(that.leftCapacity) : that.leftCapacity != null) return false;
        if (event != null ? !event.equals(that.event) : that.event != null) return false;
        if (artists != null ? !artists.equals(that.artists) : that.artists != null) return false;
        return hall != null ? hall.equals(that.hall) : that.hall == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (startDateTime != null ? startDateTime.hashCode() : 0);
        result = 31 * result + (endDateTime != null ? endDateTime.hashCode() : 0);
        result = 31 * result + (basePrice != null ? basePrice.hashCode() : 0);
        result = 31 * result + (leftCapacity != null ? leftCapacity.hashCode() : 0);
        result = 31 * result + (event != null ? event.hashCode() : 0);
        result = 31 * result + (artists != null ? artists.hashCode() : 0);
        result = 31 * result + (hall != null ? hall.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PerformanceDTO{" +
            "id=" + id +
            ", startDateTime=" + startDateTime +
            ", endDateTime=" + endDateTime +
            ", basePrice=" + basePrice +
            ", leftCapacity=" + leftCapacity +
            ", event=" + event +
            ", artists=" + artists +
            ", hall=" + hall +
            '}';
    }

    public static PerformanceDTOBuilder builder(){
        return new PerformanceDTOBuilder();
    }

    public static final class PerformanceDTOBuilder{

        private Long id;
        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;
        private Double basePrice;
        private Integer leftCapacity;
        private EventDTO event;
        private List<ArtistDTO> artists = new ArrayList<>();
        private HallDTO hall;

        public PerformanceDTOBuilder id(Long id){
            this.id = id;
            return this;
        }

        public PerformanceDTOBuilder startDateTime(LocalDateTime startDateTime){
            this.startDateTime = startDateTime;
            return this;
        }

        public PerformanceDTOBuilder endDateTime(LocalDateTime endDateTime){
            this.endDateTime = endDateTime;
            return this;
        }

        public PerformanceDTOBuilder basePrice(Double basePrice){
            this.basePrice = basePrice;
            return this;
        }

        public PerformanceDTOBuilder leftCapacity(Integer leftCapacity){
            this.leftCapacity = leftCapacity;
            return this;
        }

        public PerformanceDTOBuilder event(EventDTO event){
            this.event = event;
            return this;
        }

        public PerformanceDTOBuilder artists(List<ArtistDTO> artists){
            this.artists = artists;
            return this;
        }

        public PerformanceDTOBuilder hall(HallDTO hall){
            this.hall = hall;
            return this;
        }

        public PerformanceDTO builder(){
            PerformanceDTO performanceDTO = new PerformanceDTO();
            performanceDTO.setId(id);
            performanceDTO.setStartDateTime(startDateTime);
            performanceDTO.setEndDateTime(endDateTime);
            performanceDTO.setBasePrice(basePrice);
            performanceDTO.setLeftCapacity(leftCapacity);
            performanceDTO.setEvent(event);
            performanceDTO.setArtists(artists);
            performanceDTO.setHall(hall);
            return performanceDTO;
        }
    }
}
