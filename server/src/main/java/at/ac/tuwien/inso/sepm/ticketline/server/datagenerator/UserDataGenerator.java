package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.configuration.SecurityConfiguration;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.UserRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Profile("generateData")
@Component
public class UserDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewsDataGenerator.class);

    private final UserRepository userRepository;
    private static final int NUMBER_OF_USER_TO_GENERATE = 1000;

    public UserDataGenerator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    private void generateUsers() {
        if (userRepository.count() > 0) {
            LOGGER.info("users already generated");
            return;
        }
        LOGGER.info("generating user entries");
        List<String> userNames = new ArrayList<>();
        String password = SecurityConfiguration.configureDefaultPasswordEncoder().encode("password");
        Faker faker = new Faker();
        LocalDateTime now = LocalDateTime.now().withNano(0);
        User user;
        user = User.builder()
            .firstName("Super")
            .lastName("Admin")
            .userName("admin")
            .password(password)
            .role(User.ROLE_ADMIN)
            .activeTime(now)
            .build();
        userNames.add(user.getUserName());
        LOGGER.debug("saving user {}", user);
        userRepository.save(user);

        user = User.builder()
            .firstName("Normal")
            .lastName("User")
            .userName("user")
            .password(password)
            .role(User.ROLE_USER)
            .activeTime(now)
            .build();
        userNames.add(user.getUserName());
        LOGGER.debug("saving user {}", user);
        userRepository.save(user);

        String userName, firstName, lastName;
        List<User> users = new ArrayList<>();
        LOGGER.info("generating {} user entries", NUMBER_OF_USER_TO_GENERATE);
        for (int i = 0; i < NUMBER_OF_USER_TO_GENERATE; i++) {
            do {
                firstName = faker.name().firstName();
                lastName = faker.name().lastName();
                userName = (firstName + "." + lastName).toLowerCase();
            }while(userNames.contains(userName));
            userNames.add(userName);
            users.add(User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .userName(userName)
                .password(password)
                .role(User.ROLE_USER)
                .activeTime(now)
                .build());
        }
        userRepository.saveAll(users);
    }
}
