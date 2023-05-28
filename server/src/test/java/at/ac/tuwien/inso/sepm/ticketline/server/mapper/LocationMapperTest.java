package at.ac.tuwien.inso.sepm.ticketline.server.mapper;

import at.ac.tuwien.inso.sepm.ticketline.rest.hall.HallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.location.LocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.HallType;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.hall.HallMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.location.LocationMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class LocationMapperTest {

    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper")
    public static class LocationMapperTestContextConfiguration {
    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private LocationMapper locationMapper;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private HallMapper hallMapper;

    private static final Long LOCATION_ID = 1l;
    private static final String LOCATION_DESCRIPTION = "Description";
    private static final String LOCATION_COUNTRY = "Country";
    private static final String LOCATION_CITY = "City";
    private static final String LOCATION_ZIP = "Zip";
    private static final String LOCATION_STREET = "Street";
    private static final Integer LOCATION_HOUSNUMBER = 11;

    private static final Location LOCATION = Location.builder()
        .id(1L)
        .houseNumber(11)
        .city("City")
        .country("Country")
        .street("Street")
        .zip("Zip")
        .description("Description")
        .build();

    private static final Hall HALL = Hall.builder()
        .id(1l)
        .capacity(100)
        .description("Description")
        .type(HallType.SEAT)
        .location(LOCATION)
        .build();

    @Test
    public void shouldMapLocationDTOToLocation(){
        List<Hall> halls = new ArrayList<>();
        halls.add(HALL);
        List<HallDTO> hallDTOS = hallMapper.hallToHallDTO(halls);
        LocationDTO locationDTO = LocationDTO.builder()
            .id(LOCATION_ID)
            .houseNumber(LOCATION_HOUSNUMBER)
            .city(LOCATION_CITY)
            .country(LOCATION_COUNTRY)
            .description(LOCATION_DESCRIPTION)
            .street(LOCATION_STREET)
            .zip(LOCATION_ZIP)
            .halls(hallDTOS)
            .builder();

        Location location = locationMapper.locationDTOToLocation(locationDTO);
        assertThat(location).isNotNull();
        assertThat(location.getId()).isEqualTo(LOCATION_ID);
        assertThat(location.getCity()).isEqualTo(LOCATION_CITY);
        assertThat(location.getCountry()).isEqualTo(LOCATION_COUNTRY);
        assertThat(location.getDescription()).isEqualTo(LOCATION_DESCRIPTION);
        assertThat(location.getHouseNumber()).isEqualTo(LOCATION_HOUSNUMBER);
        assertThat(location.getStreet()).isEqualTo(LOCATION_STREET);
        assertThat(location.getZip()).isEqualTo(LOCATION_ZIP);
        assertThat(location.getHalls()).isEqualTo(halls);
    }

    @Test
    public void shouldMapLocationToLocationDTO(){
        List<Hall> halls = new ArrayList<>();
        halls.add(HALL);
        Location location = Location.builder()
            .id(LOCATION_ID)
            .houseNumber(LOCATION_HOUSNUMBER)
            .city(LOCATION_CITY)
            .country(LOCATION_COUNTRY)
            .description(LOCATION_DESCRIPTION)
            .street(LOCATION_STREET)
            .zip(LOCATION_ZIP)
            .halls(halls)
            .build();

        LocationDTO locationDTO = locationMapper.locationToLocationDTO(location);

        List<HallDTO> hallDTOS = hallMapper.hallToHallDTO(halls);
        assertThat(locationDTO).isNotNull();
        assertThat(locationDTO.getId()).isEqualTo(LOCATION_ID);
        assertThat(locationDTO.getCity()).isEqualTo(LOCATION_CITY);
        assertThat(locationDTO.getCountry()).isEqualTo(LOCATION_COUNTRY);
        assertThat(locationDTO.getDescription()).isEqualTo(LOCATION_DESCRIPTION);
        assertThat(locationDTO.getHouseNumber()).isEqualTo(LOCATION_HOUSNUMBER);
        assertThat(locationDTO.getStreet()).isEqualTo(LOCATION_STREET);
        assertThat(locationDTO.getZip()).isEqualTo(LOCATION_ZIP);
        assertThat(locationDTO.getHalls()).isEqualTo(hallDTOS);
    }
}
