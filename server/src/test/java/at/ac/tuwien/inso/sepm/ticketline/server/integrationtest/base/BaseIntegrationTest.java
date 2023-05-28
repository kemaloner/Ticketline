package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base;

import at.ac.tuwien.inso.sepm.ticketline.server.configuration.JacksonConfiguration;
import at.ac.tuwien.inso.sepm.ticketline.server.configuration.SecurityConfiguration;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.UserRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.security.AuthenticationConstants;
import at.ac.tuwien.inso.sepm.ticketline.server.service.implementation.SimpleHeaderTokenAuthenticationService;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import org.assertj.core.util.Strings;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
public abstract class BaseIntegrationTest {

    private static final String SERVER_HOST = "http://localhost";
    private static final String USER_USERNAME = "user";
    private static final String USER_PASSWORD = "password";
    private static final String ADMIN_PASSWORD = "password";
    private static final String ADMIN_USERNAME = "admin";

    @Value("${server.context-path}")
    private String contextPath;

    @LocalServerPort
    private int port;

    @Autowired
    private SimpleHeaderTokenAuthenticationService simpleHeaderTokenAuthenticationService;

    @Autowired
    private JacksonConfiguration jacksonConfiguration;

    protected String validUserTokenWithPrefix;
    protected String validAdminTokenWithPrefix;

    @Autowired
    protected UserRepository userRepository;
    protected User user,admin;

    private void usersGenerate(){
        PasswordEncoder encoder = SecurityConfiguration.configureDefaultPasswordEncoder();

        user = User.builder()
            .firstName(USER_USERNAME)
            .lastName(USER_USERNAME)
            .userName(USER_USERNAME)
            .password(encoder.encode(USER_PASSWORD))
            .role(User.ROLE_USER)
            .activeTime(LocalDateTime.now())
            .active(true)
            .build();

        admin = User.builder()
            .firstName(ADMIN_USERNAME)
            .lastName(ADMIN_USERNAME)
            .userName(ADMIN_USERNAME)
            .password(encoder.encode(ADMIN_PASSWORD))
            .role(User.ROLE_ADMIN)
            .activeTime(LocalDateTime.now())
            .active(true)
            .build();

        if(!userRepository.findOneByUserName(USER_USERNAME).isPresent()){
            userRepository.save(user);
        }

        if(!userRepository.findOneByUserName(ADMIN_USERNAME).isPresent()){
            userRepository.save(admin);
        }
    }

    @Before
    public void beforeBase() {
        usersGenerate();
        RestAssured.baseURI = SERVER_HOST;
        RestAssured.basePath = contextPath;
        RestAssured.port = port;
        RestAssured.config = RestAssuredConfig.config().
            objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory((aClass, s) ->
                jacksonConfiguration.jackson2ObjectMapperBuilder().build()));
        validUserTokenWithPrefix = Strings
            .join(
                AuthenticationConstants.TOKEN_PREFIX,
                simpleHeaderTokenAuthenticationService.authenticate(USER_USERNAME, USER_PASSWORD).getCurrentToken())
            .with(" ");
        validAdminTokenWithPrefix = Strings
            .join(
                AuthenticationConstants.TOKEN_PREFIX,
                simpleHeaderTokenAuthenticationService.authenticate(ADMIN_USERNAME, ADMIN_PASSWORD).getCurrentToken())
            .with(" ");

        user = userRepository.findOneByUserName(USER_USERNAME).get();
        admin = userRepository.findOneByUserName(ADMIN_USERNAME).get();
    }


    public String getServiceURI(String serviceLocation, MultiValueMap<String, String> params) {
        return UriComponentsBuilder.fromPath(serviceLocation).queryParams(params).build().encode().toString();
    }

    public String getServiceURI(String serviceLocation, Pageable pageable) {
        return getServiceURI(serviceLocation,pageableToMap(pageable));
    }

    public String getServiceURI(String serviceLocation, MultiValueMap<String, String> params, Pageable pageable) {
        params.addAll(pageableToMap(pageable));
        return getServiceURI(serviceLocation,params);
    }

    private MultiValueMap<String, String> pageableToMap(Pageable pageable){
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("size", ""+pageable.getPageSize());
        map.add("page", ""+pageable.getPageNumber());
        if(pageable.getSort().isSorted()) {
            pageable.getSort().forEach(order -> map.add("sort", order.getProperty() + "," + order.getDirection()));
        }
        return map;
    }
}
