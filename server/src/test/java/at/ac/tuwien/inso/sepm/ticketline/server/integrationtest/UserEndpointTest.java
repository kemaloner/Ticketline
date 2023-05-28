package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.user.UserMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;

public class UserEndpointTest extends BaseIntegrationTest {

    private static final String USER_ENDPOINT = "/user";
    private static final String SPECIFIC_USER_PATH = USER_ENDPOINT + "/{username}";

    @Autowired
    private UserMapper userMapper;

    @Test
    public void findAllUserUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(USER_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findOneUserUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(SPECIFIC_USER_PATH, user.getUserName())
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findAllUserAsUser() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(USER_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void findSelfUserAsUser() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(SPECIFIC_USER_PATH, user.getUserName())
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(SimpleUserDTO.class), is(userMapper.userToSimpleUserDTO(user)));
    }

    @Test
    public void findOtherUserAsUser() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(SPECIFIC_USER_PATH, admin.getUserName())
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void findAllUserAsAdmin() {
        List<User> users = Arrays.asList(user,admin);
        PageRequest request = PageRequest.of(0,10);
        userRepository = BDDMockito.mock(UserRepository.class);
        BDDMockito.
            given(userRepository.findAll(request)).
            willReturn(new PageImpl<>(users,request,users.size()));

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(getServiceURI(USER_ENDPOINT,request))
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        List<SimpleUserDTO> actual =  response.getBody().jsonPath().getList("entities",SimpleUserDTO.class);
        List<SimpleUserDTO> expected = userMapper.userToSimpleUserDTO(users);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void findSelfUserAsAdmin() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(SPECIFIC_USER_PATH, admin.getUserName())
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(SimpleUserDTO.class), is(userMapper.userToSimpleUserDTO(admin)));
    }

    @Test
    public void findOtherUserAsAdmin() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(SPECIFIC_USER_PATH, user.getUserName())
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(SimpleUserDTO.class), is(userMapper.userToSimpleUserDTO(user)));
    }


    @Test
    public void saveUserAsUser() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(userMapper.userToDetailedUserDTO(user))
            .when().put(USER_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void saveUserAsAdmin() {
        String name = user.getFirstName();
        name = name.equals(name.toUpperCase()) ? name.toLowerCase() : name.toUpperCase();
        user.setFirstName(name);

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(userMapper.userToDetailedUserDTO(user))
            .when().put(USER_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(DetailedUserDTO.class), is(userMapper.userToDetailedUserDTO(user)));
    }
}
