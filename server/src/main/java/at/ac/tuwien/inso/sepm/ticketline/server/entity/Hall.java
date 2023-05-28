package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_hall_id")
    @SequenceGenerator(name = "seq_hall_id", sequenceName = "seq_hall_id")
    private Long id;

    @Column(nullable = false, name = "description")
    private String description;

    @Column(nullable = false, name = "type")
    @Enumerated(EnumType.STRING)
    private HallType type;

    @Column(nullable = false, name = "capacity")
    private Integer capacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @Transient // added by anil
    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL)
    private List<Seat> seats;

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

    public HallType getType() {
        return type;
    }

    public void setType(HallType type) {
        this.type = type;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hall hall = (Hall) o;

        if (id != null ? !id.equals(hall.id) : hall.id != null) return false;
        if (description != null ? !description.equals(hall.description) : hall.description != null) return false;
        if (type != null ? !type.equals(hall.type) : hall.type != null) return false;
        if (capacity != null ? !capacity.equals(hall.capacity) : hall.capacity != null) return false;
        if (location != null ? !location.equals(hall.location) : hall.location != null) return false;
        return seats != null ? seats.equals(hall.seats) : hall.seats == null;
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
        return "Hall{" +
            "id=" + id +
            ", description='" + description + '\'' +
            ", type='" + type + '\'' +
            ", capacity=" + capacity +
            ", location=" + location +
            ", seats=" + seats +
            '}';
    }

    public static HallBuilder builder(){
        return new HallBuilder();
    }

    public static final class HallBuilder{

        private Long id;
        private String description;
        private HallType type;
        private Integer capacity;
        private Location location;
        private List<Seat> seats;

        private HallBuilder(){
        }

        public HallBuilder id(Long id){
            this.id = id;
            return this;
        }

        public HallBuilder description(String description){
            this.description = description;
            return this;
        }

        public HallBuilder type(HallType type){
            this.type = type;
            return this;
        }

        public HallBuilder capacity(Integer capacity){
            this.capacity = capacity;
            return this;
        }

        public HallBuilder location(Location location){
            this.location = location;
            return this;
        }

        public HallBuilder seats(List<Seat> seats){
            this.seats = seats;
            return this;
        }

        public Hall build(){
            Hall hall = new Hall();
            hall.setId(id);
            hall.setDescription(description);
            hall.setType(type);
            hall.setCapacity(capacity);
            hall.setLocation(location);
            hall.setSeats(seats);
            return hall;
        }

    }
}
