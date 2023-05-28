package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.user;

import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User simpleUserDTOToUser(SimpleUserDTO simpleUserDTO);

    User detailedUserDTOToUser(DetailedUserDTO detaileUserDTO);

    SimpleUserDTO userToSimpleUserDTO(User one);

    DetailedUserDTO userToDetailedUserDTO(User one);

    List<SimpleUserDTO> userToSimpleUserDTO(List<User> all);

    List<DetailedUserDTO> userToDetailedUserDTO(List<User> all);
}
