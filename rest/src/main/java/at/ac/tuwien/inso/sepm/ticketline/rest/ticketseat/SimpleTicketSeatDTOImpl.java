package at.ac.tuwien.inso.sepm.ticketline.rest.ticketseat;

public class SimpleTicketSeatDTOImpl implements SimpleTicketSeatDTO {
    private Long id;
    private Integer sector;
    private Integer row;
    private Integer number;
    private String type;
    private Double multiplier;
    private Integer x;
    private Integer y;
    private Integer angle;
    private String status;
    private Long ticketId;

    public SimpleTicketSeatDTOImpl(){}

    public SimpleTicketSeatDTOImpl(SimpleTicketSeatDTO s){
        this(s.getId(),s.getSector(),s.getRow(),s.getNumber(),s.getType(),s.getMultiplier(),s.getX(),s.getY(),s.getAngle(),s.getStatus(),s.getTicketId());
    }

    public SimpleTicketSeatDTOImpl(Long id, Integer sector, Integer row, Integer number, String type, Double multiplier, Integer x, Integer y, Integer angle, String status, Long ticketId) {
        this.id = id;
        this.sector = sector;
        this.row = row;
        this.number = number;
        this.type = type;
        this.multiplier = multiplier;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.status = status;
        this.ticketId = ticketId;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Integer getSector() {
        return sector;
    }

    @Override
    public Integer getRow() {
        return row;
    }

    @Override
    public Integer getNumber() {
        return number;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Double getMultiplier() {
        return multiplier;
    }

    @Override
    public Integer getX() {
        return x;
    }

    @Override
    public Integer getY() {
        return y;
    }

    @Override
    public Integer getAngle() {
        return angle;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public Long getTicketId() {
        return ticketId;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setSector(Integer sector) {
        this.sector = sector;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMultiplier(Double multiplier) {
        this.multiplier = multiplier;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public void setAngle(Integer angle) {
        this.angle = angle;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    @Override
    public String toString() {
        return "SimpleTicketSeatDTOImpl{" +
            "id=" + id +
            ", sector=" + sector +
            ", row=" + row +
            ", number=" + number +
            ", type='" + type + '\'' +
            ", multiplier=" + multiplier +
            ", x=" + x +
            ", y=" + y +
            ", angle=" + angle +
            ", status='" + status + '\'' +
            ", ticketId=" + ticketId +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleTicketSeatDTOImpl that = (SimpleTicketSeatDTOImpl) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (sector != null ? !sector.equals(that.sector) : that.sector != null) return false;
        if (row != null ? !row.equals(that.row) : that.row != null) return false;
        if (number != null ? !number.equals(that.number) : that.number != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (multiplier != null ? !multiplier.equals(that.multiplier) : that.multiplier != null) return false;
        if (x != null ? !x.equals(that.x) : that.x != null) return false;
        if (y != null ? !y.equals(that.y) : that.y != null) return false;
        if (angle != null ? !angle.equals(that.angle) : that.angle != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        return ticketId != null ? ticketId.equals(that.ticketId) : that.ticketId == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (sector != null ? sector.hashCode() : 0);
        result = 31 * result + (row != null ? row.hashCode() : 0);
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (multiplier != null ? multiplier.hashCode() : 0);
        result = 31 * result + (x != null ? x.hashCode() : 0);
        result = 31 * result + (y != null ? y.hashCode() : 0);
        result = 31 * result + (angle != null ? angle.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (ticketId != null ? ticketId.hashCode() : 0);
        return result;
    }

    public SimpleTicketSeatDTOBuilder builder(){
        return new SimpleTicketSeatDTOBuilder();
    }

    private static class SimpleTicketSeatDTOBuilder{
        private Long id;
        private Integer sector;
        private Integer row;
        private Integer number;
        private String type;
        private Double multiplier;
        private Integer x;
        private Integer y;
        private Integer angle;
        private String status;
        private Long ticketId;

        public SimpleTicketSeatDTOBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public SimpleTicketSeatDTOBuilder sector(Integer sector) {
            this.sector = sector;
            return this;
        }

        public SimpleTicketSeatDTOBuilder row(Integer row) {
            this.row = row;
            return this;
        }

        public SimpleTicketSeatDTOBuilder number(Integer number) {
            this.number = number;
            return this;
        }

        public SimpleTicketSeatDTOBuilder type(String type) {
            this.type = type;
            return this;
        }

        public SimpleTicketSeatDTOBuilder multiplier(Double multiplier) {
            this.multiplier = multiplier;
            return this;
        }

        public SimpleTicketSeatDTOBuilder x(Integer x) {
            this.x = x;
            return this;
        }

        public SimpleTicketSeatDTOBuilder y(Integer y) {
            this.y = y;
            return this;
        }

        public SimpleTicketSeatDTOBuilder angle(Integer angle) {
            this.angle = angle;
            return this;
        }

        public SimpleTicketSeatDTOBuilder status(String status) {
            this.status = status;
            return this;
        }

        public SimpleTicketSeatDTOBuilder ticketId(Long ticketId) {
            this.ticketId = ticketId;
            return this;
        }

        public SimpleTicketSeatDTOImpl build(){
            return new SimpleTicketSeatDTOImpl(id,sector,row,number,type,multiplier,x,y,angle,status,ticketId);
        }
    }

}
