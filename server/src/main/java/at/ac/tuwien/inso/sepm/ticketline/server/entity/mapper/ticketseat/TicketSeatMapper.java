package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.ticketseat;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticketseat.TicketSeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketSeat;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TicketSeatMapper {

    TicketSeat ticketSeatDTOToTicketSeat(TicketSeatDTO ticketDTO);

    TicketSeatDTO ticketSeatToTicketSeatDTO(TicketSeat ticket);

    List<TicketSeatDTO> ticketSeatToTicketSeatDTO(List<TicketSeat> ticket);

    List<TicketSeat> ticketSeatDTOToTicketSeat(List<TicketSeatDTO> ticketSeatDTOS);
}
