package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_performance_id")
    @SequenceGenerator(name = "seq_performance_id", sequenceName = "seq_performance_id")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @Column(nullable = false)
    private Double basePrice;

    @Column(nullable = false)
    private Integer leftCapacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToMany(mappedBy = "plays_in", fetch = FetchType.EAGER)
    private List<Artist> artists = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id")
    private Hall hall;

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

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Performance that = (Performance) o;

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
        return "Performance{" +
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

    public static PerformanceBuilder builder(){
        return new PerformanceBuilder();
    }

    public static final class PerformanceBuilder{

        private Long id;
        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;
        private Double basePrice;
        private Integer leftCapacity;
        private Event event;
        private List<Artist> artists = new ArrayList<>();
        private Hall hall;

        private PerformanceBuilder(){
        }

        public PerformanceBuilder id(Long id){
            this.id = id;
            return this;
        }

        public PerformanceBuilder startDateTime(LocalDateTime startDateTime){
            this.startDateTime = startDateTime;
            return this;
        }

        public PerformanceBuilder endDateTime(LocalDateTime endDateTime){
            this.endDateTime = endDateTime;
            return this;
        }

        public PerformanceBuilder basePrice(Double basePrice){
            this.basePrice = basePrice;
            return this;
        }

        public PerformanceBuilder leftCapacity(Integer leftCapacity){
            this.leftCapacity = leftCapacity;
            return this;
        }

        public PerformanceBuilder event(Event event){
            this.event = event;
            return this;
        }

        public PerformanceBuilder artists(List<Artist> artists){
            this.artists = artists;
            return this;
        }

        public PerformanceBuilder hall(Hall hall){
            this.hall = hall;
            return this;
        }

        public Performance build(){
            Performance performance = new Performance();
            performance.setId(id);
            performance.setStartDateTime(startDateTime);
            performance.setEndDateTime(endDateTime);
            performance.setBasePrice(basePrice);
            performance.setLeftCapacity(leftCapacity);
            performance.setEvent(event);
            performance.setArtists(artists);
            performance.setHall(hall);
            return performance;
        }
    }

}
