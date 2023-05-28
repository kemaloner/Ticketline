package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.artist;

import at.ac.tuwien.inso.sepm.ticketline.rest.artist.ArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtistMapper {

    Artist artistDTOToArtist(ArtistDTO artistDTO);

    ArtistDTO artistToArtistDTO(Artist artist);

    List<ArtistDTO> artistToArtistDTO(List<Artist> artists);
}
