package at.ac.tuwien.inso.sepm.ticketline.rest.event;

import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.List;

@ApiModel(value = "EventDTO", description = "A simple DTO for event entries via rest")
public class EventDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(required = true, readOnly = true, name = "The category of the event")
    private String category;

    @ApiModelProperty(required = true, readOnly = true, name = "The title of the event")
    private String title;

    @ApiModelProperty(required = true, readOnly = true, name = "The description of the event")
    private String description;

    @ApiModelProperty(required = true, readOnly = true, name = "The start date of the event")
    private LocalDateTime startDate;

    @ApiModelProperty(required = true, readOnly = true, name = "The end date of the event")
    private LocalDateTime endDate;

    @ApiModelProperty(required = true, readOnly = true, name = "The duration of the event")
    private Integer duration;

    @ApiModelProperty(required = true, readOnly = true, name = "The list of performance of the event")
    private List<PerformanceDTO> performances;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public List<PerformanceDTO> getPerformances() {
        return performances;
    }

    public void setPerformances(List<PerformanceDTO> performances) {
        this.performances = performances;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventDTO eventDTO = (EventDTO) o;

        if (id != null ? !id.equals(eventDTO.id) : eventDTO.id != null) return false;
        if (category != null ? !category.equals(eventDTO.category) : eventDTO.category != null) return false;
        if (title != null ? !title.equals(eventDTO.title) : eventDTO.title != null) return false;
        if (description != null ? !description.equals(eventDTO.description) : eventDTO.description != null)
            return false;
        if (startDate != null ? !startDate.equals(eventDTO.startDate) : eventDTO.startDate != null) return false;
        if (endDate != null ? !endDate.equals(eventDTO.endDate) : eventDTO.endDate != null) return false;
        if (duration != null ? !duration.equals(eventDTO.duration) : eventDTO.duration != null) return false;
        return performances != null ? performances.equals(eventDTO.performances) : eventDTO.performances == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (performances != null ? performances.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EventDTO{" +
            "id=" + id +
            ", category='" + category + '\'' +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", duration=" + duration +
            ", performances=" + performances +
            '}';
    }

    public static EventDTOBuilder builder(){
        return new EventDTOBuilder();
    }

    public static final class EventDTOBuilder{

        private Long id;
        private String category;
        private String title;
        private String description;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private Integer duration;
        private List<PerformanceDTO> performances;

        public EventDTOBuilder id(Long id){
            this.id = id;
            return this;
        }

        public EventDTOBuilder category(String category){
            this.category = category;
            return this;
        }

        public EventDTOBuilder title(String title){
            this.title = title;
            return this;
        }

        public EventDTOBuilder description(String description){
            this.description = description;
            return this;
        }

        public EventDTOBuilder startDate(LocalDateTime startDate){
            this.startDate = startDate;
            return this;
        }

        public EventDTOBuilder endDate(LocalDateTime endDate){
            this.endDate = endDate;
            return this;
        }

        public EventDTOBuilder duration(Integer duration){
            this.duration = duration;
            return this;
        }

        public EventDTOBuilder performances(List<PerformanceDTO> performances){
            this.performances = performances;
            return this;
        }

        public EventDTO builder(){
            EventDTO eventDTO = new EventDTO();
            eventDTO.setId(id);
            eventDTO.setCategory(category);
            eventDTO.setTitle(title);
            eventDTO.setDescription(description);
            eventDTO.setStartDate(startDate);
            eventDTO.setEndDate(endDate);
            eventDTO.setDuration(duration);
            eventDTO.setPerformances(performances);
            return eventDTO;
        }
    }
}
