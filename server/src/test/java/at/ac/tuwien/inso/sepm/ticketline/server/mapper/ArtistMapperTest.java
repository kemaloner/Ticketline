package at.ac.tuwien.inso.sepm.ticketline.server.mapper;

import at.ac.tuwien.inso.sepm.ticketline.rest.artist.ArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.artist.ArtistMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class ArtistMapperTest {

    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper")
    public static class ArtistMapperTestContextConfiguration {
    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private ArtistMapper artistMapper;

    private static final Long ARTIST_ID = 1L;
    private static final String FIRSTNAME = "Firstname";
    private static final String SURNAME = "Surname";

    @Test
    public void shouldMapArtistDTOToArtist(){
        ArtistDTO artistDTO = ArtistDTO.builder()
            .id(ARTIST_ID)
            .firstname(FIRSTNAME)
            .surname(SURNAME)
            .builder();

        Artist artist = artistMapper.artistDTOToArtist(artistDTO);
        assertThat(artist).isNotNull();
        assertThat(artist.getId()).isEqualTo(ARTIST_ID);
        assertThat(artist.getFirstname()).isEqualTo(FIRSTNAME);
        assertThat(artist.getSurname()).isEqualTo(SURNAME);
    }

    @Test
    public void shouldMapArtistToArtistDTO(){
        Artist artist = Artist.builder()
            .id(ARTIST_ID)
            .firstname(FIRSTNAME)
            .lastname(SURNAME)
            .build();

        ArtistDTO artistDTO = artistMapper.artistToArtistDTO(artist);
        assertThat(artistDTO).isNotNull();
        assertThat(artistDTO.getId()).isEqualTo(ARTIST_ID);
        assertThat(artistDTO.getFirstname()).isEqualTo(FIRSTNAME);
        assertThat(artistDTO.getSurname()).isEqualTo(SURNAME);
    }
}
