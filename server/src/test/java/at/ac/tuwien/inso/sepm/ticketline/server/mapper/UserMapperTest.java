package at.ac.tuwien.inso.sepm.ticketline.server.mapper;

import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.user.UserMapper;
import org.junit.Before;
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
public class UserMapperTest {

    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper")
    public static class UserMapperTestContextConfiguration {
    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    private UserMapper userMapper;

    private static final Long USER_ID = 101L;
    private static final String USER_FIRSTNAME = "Max";
    private static final String USER_LASTNAME = "Musterman";
    private static final String USER_NAME = "MaxMusterman";
    private static final String USER_PASSWORD = "1234";
    private static final Integer USER_ROLE = 1;
    private static final boolean USER_ISACTIVE = true;
    private static final Integer USER_ATTEMPTS = User.ATTEMPTS;
    private static final Integer NEWS_COUNT = 7;
    private static final List<News> USER_READNEWS = new ArrayList<>();
    private static final List<SimpleNewsDTO> SIMPLE_NEWS_DTOS = new ArrayList<>();

    @Before
    public void setUp(){
        for (long i = USER_READNEWS.size(); i < NEWS_COUNT; i++) {
            USER_READNEWS.add(News.builder().id(i).build());
            SIMPLE_NEWS_DTOS.add(SimpleNewsDTO.builder().id(i).build());
        }
    }

    @Test
    public void shouldMapUserToSimpleUserDTO() {
        User user = User.builder()
            .id(USER_ID)
            .firstName(USER_FIRSTNAME)
            .lastName(USER_LASTNAME)
            .userName(USER_NAME)
            .password(USER_PASSWORD)
            .role(USER_ROLE)
            .active(USER_ISACTIVE)
            .attempts(USER_ATTEMPTS)
            .build();
        SimpleUserDTO dto = userMapper.userToSimpleUserDTO(user);
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(USER_ID);
        assertThat(dto.getFirstName()).isEqualTo(USER_FIRSTNAME);
        assertThat(dto.getLastName()).isEqualTo(USER_LASTNAME);
        assertThat(dto.getUserName()).isEqualTo(USER_NAME);
        assertThat(dto.getPassword()).isEqualTo(USER_PASSWORD);
        assertThat(dto.getRole()).isEqualTo(USER_ROLE);
        assertThat(dto.isActive()).isEqualTo(USER_ISACTIVE);
        assertThat(dto.getAttempts()).isEqualTo(USER_ATTEMPTS);
    }

    @Test
    public void shouldMapSimpleUserDTOToUser() {
        SimpleUserDTO dto = SimpleUserDTO.builder()
            .id(USER_ID)
            .firstName(USER_FIRSTNAME)
            .lastName(USER_LASTNAME)
            .userName(USER_NAME)
            .password(USER_PASSWORD)
            .role(USER_ROLE)
            .active(USER_ISACTIVE)
            .attempts(USER_ATTEMPTS)
            .build();
        User user = userMapper.simpleUserDTOToUser(dto);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(USER_ID);
        assertThat(user.getFirstName()).isEqualTo(USER_FIRSTNAME);
        assertThat(user.getLastName()).isEqualTo(USER_LASTNAME);
        assertThat(user.getUserName()).isEqualTo(USER_NAME);
        assertThat(user.getPassword()).isEqualTo(USER_PASSWORD);
        assertThat(user.getRole()).isEqualTo(USER_ROLE);
        assertThat(user.isActive()).isEqualTo(USER_ISACTIVE);
        assertThat(user.getAttempts()).isEqualTo(USER_ATTEMPTS);
        assertThat(user.getReadNews().size()).isEqualTo(0);
    }

    @Test
    public void shouldMapUserToDetailedUserDTO() {
        User user = User.builder()
            .id(USER_ID)
            .firstName(USER_FIRSTNAME)
            .lastName(USER_LASTNAME)
            .userName(USER_NAME)
            .password(USER_PASSWORD)
            .role(USER_ROLE)
            .active(USER_ISACTIVE)
            .attempts(USER_ATTEMPTS)
            .readNews(USER_READNEWS)
            .build();
        DetailedUserDTO dto = userMapper.userToDetailedUserDTO(user);
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(USER_ID);
        assertThat(dto.getFirstName()).isEqualTo(USER_FIRSTNAME);
        assertThat(dto.getLastName()).isEqualTo(USER_LASTNAME);
        assertThat(dto.getUserName()).isEqualTo(USER_NAME);
        assertThat(dto.getPassword()).isEqualTo(USER_PASSWORD);
        assertThat(dto.getRole()).isEqualTo(USER_ROLE);
        assertThat(dto.isActive()).isEqualTo(USER_ISACTIVE);
        assertThat(dto.getAttempts()).isEqualTo(USER_ATTEMPTS);
        assertThat(dto.getReadNews().size()).isEqualTo(NEWS_COUNT);
        assertThat(dto.getReadNews()).isEqualTo(SIMPLE_NEWS_DTOS);
    }

    @Test
    public void shouldMapDetailedUserDTOToUser() {
        DetailedUserDTO dto = DetailedUserDTO.builder()
            .id(USER_ID)
            .firstName(USER_FIRSTNAME)
            .lastName(USER_LASTNAME)
            .userName(USER_NAME)
            .password(USER_PASSWORD)
            .role(USER_ROLE)
            .active(USER_ISACTIVE)
            .attempts(USER_ATTEMPTS)
            .readNews(SIMPLE_NEWS_DTOS)
            .build();
        User user = userMapper.detailedUserDTOToUser(dto);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(USER_ID);
        assertThat(user.getFirstName()).isEqualTo(USER_FIRSTNAME);
        assertThat(user.getLastName()).isEqualTo(USER_LASTNAME);
        assertThat(user.getUserName()).isEqualTo(USER_NAME);
        assertThat(user.getPassword()).isEqualTo(USER_PASSWORD);
        assertThat(user.getRole()).isEqualTo(USER_ROLE);
        assertThat(user.isActive()).isEqualTo(USER_ISACTIVE);
        assertThat(user.getAttempts()).isEqualTo(USER_ATTEMPTS);
        assertThat(user.getReadNews()).isNotNull();
        assertThat(user.getReadNews().size()).isEqualTo(NEWS_COUNT);
        //assertThat(user.getReadNews()).isEqualTo(USER_READNEWS);
    }

}
