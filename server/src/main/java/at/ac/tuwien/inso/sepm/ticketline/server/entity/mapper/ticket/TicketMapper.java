package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.ticket;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    Ticket ticketDTOToTicket(TicketDTO ticketDTO);

    TicketDTO ticketToTicketDTO(Ticket ticket);

    List<TicketDTO> ticketToTicketDTO(List<Ticket> ticket);
}
