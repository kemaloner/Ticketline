package at.ac.tuwien.inso.sepm.ticketline.rest.seat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "SeatDTO", description = "A detailed DTO for seat entries via rest")
public class SeatDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(required = true, readOnly = true, name = "The sector of the seat")
    private Integer sector;

    @ApiModelProperty(required = true, readOnly = true, name = "The row of the seat")
    private Integer row;

    @ApiModelProperty(required = true, readOnly = true, name = "The number of the seat")
    private Integer number;

    @ApiModelProperty(required = true, readOnly = true, name = "The price factor of the seat")
    private Double multiplier;

    @ApiModelProperty(required = true, readOnly = true, name = "The x coordinate of the seat on screen")
    private Integer xCoordinate;

    @ApiModelProperty(required = true, readOnly = true, name = "The y coordinate of the seat on screen")
    private Integer yCoordinate;

    @ApiModelProperty(required = true, readOnly = true, name = "The orientation of the seat on screen")
    private Integer angle;

    @ApiModelProperty(required = true, readOnly = true, name = "The type of the seat")
    private String type;

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

    public Integer getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(Integer xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public Double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(Double multiplier) {
        this.multiplier = multiplier;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SeatDTO{" +
            "id=" + id +
            ", sector=" + sector +
            ", row=" + row +
            ", number=" + number +
            ", multiplier=" + multiplier +
            ", xCoordinate=" + xCoordinate +
            ", yCoordinate=" + yCoordinate +
            ", angle=" + angle +
            ", type='" + type + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SeatDTO seatDTO = (SeatDTO) o;

        if (id != null ? !id.equals(seatDTO.id) : seatDTO.id != null) return false;
        if (sector != null ? !sector.equals(seatDTO.sector) : seatDTO.sector != null) return false;
        if (row != null ? !row.equals(seatDTO.row) : seatDTO.row != null) return false;
        if (number != null ? !number.equals(seatDTO.number) : seatDTO.number != null) return false;
        if (multiplier != null ? !multiplier.equals(seatDTO.multiplier) : seatDTO.multiplier != null) return false;
        if (xCoordinate != null ? !xCoordinate.equals(seatDTO.xCoordinate) : seatDTO.xCoordinate != null) return false;
        if (yCoordinate != null ? !yCoordinate.equals(seatDTO.yCoordinate) : seatDTO.yCoordinate != null) return false;
        if (angle != null ? !angle.equals(seatDTO.angle) : seatDTO.angle != null) return false;
        return type != null ? type.equals(seatDTO.type) : seatDTO.type == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (sector != null ? sector.hashCode() : 0);
        result = 31 * result + (row != null ? row.hashCode() : 0);
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (multiplier != null ? multiplier.hashCode() : 0);
        result = 31 * result + (xCoordinate != null ? xCoordinate.hashCode() : 0);
        result = 31 * result + (yCoordinate != null ? yCoordinate.hashCode() : 0);
        result = 31 * result + (angle != null ? angle.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    public static SeatDTOBuilder builder(){
        return new SeatDTOBuilder();
    }

    public static final class SeatDTOBuilder{
        private Long id;
        private Integer sector;
        private Integer row;
        private Integer number;
        private Double multiplier;
        private Integer xCoordinate;
        private Integer yCoordinate;
        private Integer angle;
        private String type;

        public SeatDTOBuilder id(Long id){
            this.id = id;
            return this;
        }

        public SeatDTOBuilder sector(Integer sector){
            this.sector = sector;
            return this;
        }

        public SeatDTOBuilder row(Integer row){
            this.row = row;
            return this;
        }

        public SeatDTOBuilder number(Integer number){
            this.number = number;
            return this;
        }

        public SeatDTOBuilder multiplier(Double multiplier){
            this.multiplier = multiplier;
            return this;
        }

        public SeatDTOBuilder xCoordinate(Integer xCoordinate){
            this.xCoordinate = xCoordinate;
            return this;
        }

        public SeatDTOBuilder yCoordinate(Integer yCoordinate){
            this.yCoordinate = yCoordinate;
            return this;
        }

        public SeatDTOBuilder angle(Integer angle){
            this.angle = angle;
            return this;
        }

        public SeatDTOBuilder type(String type){
            this.type = type;
            return this;
        }

        public SeatDTO build(){
            SeatDTO seatDTO = new SeatDTO();
            seatDTO.setId(id);
            seatDTO.setSector(sector);
            seatDTO.setRow(row);
            seatDTO.setNumber(number);
            seatDTO.setMultiplier(multiplier);
            seatDTO.setxCoordinate(xCoordinate);
            seatDTO.setyCoordinate(yCoordinate);
            seatDTO.setAngle(angle);
            seatDTO.setType(type);
            return seatDTO;
        }
    }
}
