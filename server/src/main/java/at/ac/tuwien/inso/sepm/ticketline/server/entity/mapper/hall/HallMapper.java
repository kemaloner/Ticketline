package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.hall;

import at.ac.tuwien.inso.sepm.ticketline.rest.hall.HallDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Hall;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HallMapper {

    Hall hallDTOToHall(HallDTO hallDTO);

    HallDTO hallToHallDTO(Hall hall);

    List<HallDTO> hallToHallDTO(List<Hall> hall);
}
