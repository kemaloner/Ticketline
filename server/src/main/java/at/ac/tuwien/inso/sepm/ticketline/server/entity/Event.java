package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import org.hibernate.annotations.Check;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity

@SqlResultSetMapping(
    name = "simpleStatisticMapping",
    columns= {
        @ColumnResult(name = "id", type = Long.class),
        @ColumnResult(name = "title", type = String.class),
        @ColumnResult(name = "count", type = Integer.class)
    }
)
@NamedNativeQuery(
    name="Event.findTop10Event",
    resultSetMapping="simpleStatisticMapping",
    query="SELECT TOP 10 e.id, e.TITLE, COUNT(e.ID) as count FROM EVENT e " +
                "JOIN PERFORMANCE p ON e.ID = p.EVENT_ID JOIN TICKET t ON p.ID = t.PERFORMANCE_ID JOIN TICKET_SEAT s ON t.ID = s.TICKET_ID " +
                "WHERE p.LEFT_CAPACITY > 0 AND p.START_DATE_TIME >= :startDateTime AND p.END_DATE_TIME <= :endDateTime AND (:isCategory = FALSE OR e.CATEGORY = :category) AND s.STATUS != 'FREE'" +
                "GROUP BY e.ID " +
                "ORDER BY COUNT(e.ID) DESC")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_event_id")
    @SequenceGenerator(name = "seq_event_id", sequenceName = "seq_event_id")
    private Long id;

    @Column(nullable =  false)
    @Enumerated(EnumType.STRING)
    private EventCategory category;

    @Column(nullable = false)
    @Size(max = 100)
    private String title;

    @Column(nullable = false)
    @Size(max = 10000)
    private String description;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    @Check(constraints = "duration > 0")
    private Integer duration;

    @Transient
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Performance> performances = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventCategory getCategory() {
        return category;
    }

    public void setCategory(EventCategory category) {
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

    public List<Performance> getPerformances() {
        return performances;
    }

    public void setPerformances(List<Performance> performances) {
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

        Event event = (Event) o;

        if (id != null ? !id.equals(event.id) : event.id != null) return false;
        if (category != event.category) return false;
        if (title != null ? !title.equals(event.title) : event.title != null) return false;
        if (description != null ? !description.equals(event.description) : event.description != null) return false;
        if (startDate != null ? !startDate.equals(event.startDate) : event.startDate != null) return false;
        if (endDate != null ? !endDate.equals(event.endDate) : event.endDate != null) return false;
        if (duration != null ? !duration.equals(event.duration) : event.duration != null) return false;
        return performances != null ? performances.equals(event.performances) : event.performances == null;
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
        return "Event{" +
            "id=" + id +
            ", category=" + category +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", duration=" + duration +
            ", performances=" + performances +
            '}';
    }

    public static EventBuilder builder(){
        return new EventBuilder();
    }

    public static final class EventBuilder{

        private Long id;
        private EventCategory category;
        private String title;
        private String description;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private Integer duration;
        private List<Performance> performances;

        private EventBuilder(){
        }

        public EventBuilder id(Long id){
            this.id = id;
            return this;
        }

        public EventBuilder category(EventCategory category){
            this.category = category;
            return this;
        }

        public EventBuilder title(String title){
            this.title = title;
            return this;
        }

        public EventBuilder description(String description){
            this.description = description;
            return this;
        }

        public EventBuilder startDate(LocalDateTime startDate){
            this.startDate = startDate;
            return this;
        }

        public EventBuilder endDate(LocalDateTime endDate){
            this.endDate = endDate;
            return this;
        }

        public EventBuilder duration(Integer duration){
            this.duration = duration;
            return this;
        }

        public EventBuilder performances(List<Performance> performances){
            this.performances = performances;
            return this;
        }

        public Event build(){
            Event event = new Event();
            event.setId(id);
            event.setCategory(category);
            event.setDescription(description);
            event.setTitle(title);
            event.setStartDate(startDate);
            event.setEndDate(endDate);
            event.setDuration(duration);
            event.setPerformances(performances);
            return event;
        }
    }
}
