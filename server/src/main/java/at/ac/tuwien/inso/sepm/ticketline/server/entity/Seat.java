package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_seat_id")
    @SequenceGenerator(name = "seq_seat_id", sequenceName = "seq_seat_id")
    private Long id;

    @Column(nullable = false, name = "sector")
    private Integer sector;

    @Column(nullable = false, name = "row")
    private Integer row;

    @Column(nullable = false, name = "number")
    private Integer number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id")
    private Hall hall;

    @Column(nullable = false, name = "type")
    @Enumerated(EnumType.STRING)
    private SeatType type;

    @Column(nullable = false, name = "multiplier")
    private Double multiplier;

    @Column(nullable = false, name = "xCoordinate")
    private Integer xCoordinate;

    @Column(nullable = false, name = "yCoordinate")
    private Integer yCoordinate;

    @Column(nullable = false, name = "angle")
    private Integer angle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSector() {
        return sector;
    }

    public void setSector(Integer sector) {
        this.sector = sector;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public SeatType getType() {
        return type;
    }

    public void setType(SeatType type) {
        this.type = type;
    }

    public Integer getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(Integer xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public Integer getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(Integer yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public Integer getAngle() {
        return angle;
    }

    public void setAngle(Integer angle) {
        this.angle = angle;
    }

    public Double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(Double multiplier) {
        this.multiplier = multiplier;
    }

    public static SeatBuilder builder(){
        return new SeatBuilder();
    }

    @Override
    public String toString() {
        return "Seat{" +
            "id=" + id +
            ", sector=" + sector +
            ", row=" + row +
            ", number=" + number +
            ", hall=" + hall +
            ", type=" + type +
            ", multiplier=" + multiplier +
            ", xCoordinate=" + xCoordinate +
            ", yCoordinate=" + yCoordinate +
            ", angle=" + angle +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return Objects.equals(id, seat.id) &&
            Objects.equals(sector, seat.sector) &&
            Objects.equals(row, seat.row) &&
            Objects.equals(number, seat.number) &&
            Objects.equals(hall, seat.hall) &&
            type == seat.type &&
            Objects.equals(multiplier, seat.multiplier) &&
            Objects.equals(xCoordinate, seat.xCoordinate) &&
            Objects.equals(yCoordinate, seat.yCoordinate) &&
            Objects.equals(angle, seat.angle);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, sector, row, number, hall, type, multiplier, xCoordinate, yCoordinate, angle);
    }

    public static final class SeatBuilder{

        private Long id;
        private Integer sector;
        private Integer row;
        private Integer number;
        private Hall hall;
        private SeatType type;
        private Double multiplier;
        private Integer xCoordinate;
        private Integer yCoordinate;
        private Integer angle;

        private SeatBuilder(){
        }

        public SeatBuilder seat(Seat seat){
            id = seat.id;
            sector = seat.sector;
            row = seat.row;
            number = seat.number;
            hall = seat.hall;
            type = seat.type;
            multiplier = seat.multiplier;
            xCoordinate = seat.xCoordinate;
            yCoordinate = seat.yCoordinate;
            angle = seat.angle;
            return this;
        }

        public SeatBuilder id(Long id){
            this.id = id;
            return this;
        }

        public SeatBuilder sector(Integer sector){
            this.sector = sector;
            return this;
        }

        public SeatBuilder row(Integer row){
            this.row = row;
            return this;
        }

        public SeatBuilder number(Integer number){
            this.number = number;
            return this;
        }

        public SeatBuilder hall(Hall hall){
            this.hall = hall;
            return this;
        }

        public SeatBuilder type(SeatType type){
            this.type = type;
            return this;
        }

        public SeatBuilder multiplier(Double multiplier){
            this.multiplier = multiplier;
            return this;
        }

        public SeatBuilder xCoordinate(Integer xCoordinate){
            this.xCoordinate = xCoordinate;
            return this;
        }

        public SeatBuilder yCoordinate(Integer yCoordinate){
            this.yCoordinate = yCoordinate;
            return this;
        }

        public SeatBuilder angle(Integer angle){
            this.angle = angle;
            return this;
        }

        public Seat build(){
            Seat seat = new Seat();
            seat.setId(id);
            seat.setSector(sector);
            seat.setRow(row);
            seat.setNumber(number);
            seat.setHall(hall);
            seat.setType(type);
            seat.setMultiplier(multiplier);
            seat.setxCoordinate(xCoordinate);
            seat.setyCoordinate(yCoordinate);
            seat.setAngle(angle);
            return seat;
        }
    }
}
