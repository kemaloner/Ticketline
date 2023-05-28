package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.seat;

import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Seat;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SeatMapper {

    Seat seatDTOToSeat(SeatDTO seatDTO);

    SeatDTO seatToSeatDTO(Seat seat);

    List<SeatDTO> seatToSeatDTO(List<Seat> seat);
}
