package at.ac.tuwien.inso.sepm.ticketline.server.unittest;

import at.ac.tuwien.inso.sepm.ticketline.server.configuration.SecurityConfiguration;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.UserRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.implementation.SimpleUserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;


@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
public class UserTest {
    private static final Long USER_ID = 1L;
    private static final String USER_USERNAME = "user";
    private static final String USER_PASSWORD = "password";
    private static final Long ADMIN_ID = 1L;
    private static final String ADMIN_PASSWORD = "password";
    private static final String ADMIN_USERNAME = "admin";

    private List<User> users;
    private User user,admin;
    private Map<Long, User> userMap;

    @MockBean
    private UserRepository userRepository;

    //@Autowired
    private UserService userService;

    @Before
    public void setUp(){
        userService = new SimpleUserService(userRepository);
        user = User.builder()
            .id(USER_ID)
            .firstName(USER_USERNAME)
            .lastName(USER_USERNAME)
            .userName(USER_USERNAME)
            .password(SecurityConfiguration.configureDefaultPasswordEncoder().encode(USER_PASSWORD))
            .role(User.ROLE_USER)
            .activeTime(LocalDateTime.now())
            .active(true)
            .build();

        admin = User.builder()
            .id(ADMIN_ID)
            .firstName(ADMIN_USERNAME)
            .lastName(ADMIN_USERNAME)
            .userName(ADMIN_USERNAME)
            .password(SecurityConfiguration.configureDefaultPasswordEncoder().encode(ADMIN_PASSWORD))
            .role(User.ROLE_ADMIN)
            .activeTime(LocalDateTime.now())
            .active(true)
            .build();

        users = new ArrayList<>();
        users.add(user);
        users.add(admin);

        userMap = new HashMap<>();
        userMap.put(USER_ID, user);
        userMap.put(ADMIN_ID, admin);

        BDDMockito.doAnswer(inv -> {
            User user1 = inv.getArgument(0);
            if (user1.getId() == null){
                user1.setId(1L + userMap.size());
            }
            userMap.put(user1.getId(),user1);
            return user1;
        }).when(userRepository).save(any(User.class));
    }

    @Test
    public void findAllUsers(){
        PageRequest request = PageRequest.of(1,10);
        BDDMockito.
            given(userRepository.findAll(request)).
            willReturn(new PageImpl<User>(users,request,users.size()));

        Page<User> page = userService.findAll(request);

        assertEquals(users, page.getContent());
    }

    @Test
    public void findOneUser(){
        Long userID = 1L;
        BDDMockito.
            given(userRepository.findById(userID)).
            willReturn(Optional.of(user));

        User actualUser = userService.findOne(userID);

        assertEquals(user, actualUser);
    }

    @Test
    public void findOneUserByUserName(){
        BDDMockito.
            given(userRepository.findOneByUserName(ADMIN_USERNAME)).
            willReturn(Optional.of(admin));

        User actualUser = userService.findOneByUserName(ADMIN_USERNAME);

        assertEquals(admin, actualUser);
    }

    @Test
    public void findUsersByKeyword(){
        users.remove(user);
        PageRequest request = PageRequest.of(1,10);
        BDDMockito.
            given(userRepository.findByKeyword(ADMIN_USERNAME,request)).
            willReturn(new PageImpl<User>(users,request,users.size()));


        Page<User> page = userService.findByKeyword(ADMIN_USERNAME,request);

        assertEquals(users, page.getContent());
    }

    @Test
    public void saveUser(){
        User updateUser = User.builder()
            .id(user.getId())
            .firstName("Max")
            .lastName(user.getLastName())
            .userName(user.getUserName())
            .password(user.getPassword())
            .role(user.getRole())
            .activeTime(user.getActiveTime())
            .active(user.isActive())
            .build();

        userService.save(updateUser);

        assertEquals("Max", userMap.get(USER_ID).getFirstName());
    }

    @Test
    public void decreaseAttempts(){
        userService.decreaseAttemts(user);

        assertEquals(User.ATTEMPTS - 1, (int)userMap.get(USER_ID).getAttempts());
    }

    @Test
    public void decreaseUserAttemptsAndBlock(){
        for (int i = 0; i < User.ATTEMPTS; i++) {
            userService.decreaseAttemts(user);
        }
        User actualUser = userMap.get(USER_ID);
        assertEquals(0, (int)actualUser.getAttempts());
        assertTrue(actualUser.getActiveTime().isAfter(LocalDateTime.now()));
    }

    @Test
    public void resetUserAttempts(){
        user.setAttempts(2);
        userService.resetAttemts(user);

        assertEquals(User.ATTEMPTS, userMap.get(USER_ID).getAttempts());
    }

    @Test
    public void setActive(){
        user.setActive(false);
        userService.setActive(user,true);
        assertTrue(user.isActive());
    }

}
