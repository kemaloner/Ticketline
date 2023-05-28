package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.location;

import at.ac.tuwien.inso.sepm.ticketline.rest.location.LocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Location;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    Location locationDTOToLocation(LocationDTO locationDTO);

    LocationDTO locationToLocationDTO(Location location);

    List<LocationDTO> locationToLocationDTO(List<Location> location);
}
