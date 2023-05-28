package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;


import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationTokenInfo;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.user.UserMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.ForbiddenException;
import at.ac.tuwien.inso.sepm.ticketline.server.security.AuthenticationConstants;
import at.ac.tuwien.inso.sepm.ticketline.server.service.HeaderTokenAuthenticationService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value = "/user")
@Api(value = "user")
public class UserEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserEndpoint.class);


    @Autowired
    private HeaderTokenAuthenticationService authenticationService;

    private final UserService userService;
    private final UserMapper userMapper;

    public UserEndpoint(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Get list of simple user entries")
    public PaginationWrapper<SimpleUserDTO> findAll(Pageable pageable) {
        LOGGER.info("Loading all users");
        Page<User> page = userService.findAll(pageable);
        return new PaginationWrapper<>(userMapper.userToSimpleUserDTO(page.getContent()), page.getTotalPages());
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    @ApiOperation(value = "Get detailed information about a specific user entry")
    public DetailedUserDTO find(@ApiIgnore @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorizationHeader, @PathVariable String username) {
        LOGGER.info("Loading detailed information about user with name " + username);
        AuthenticationTokenInfo info = authenticationService.authenticationTokenInfo(authorizationHeader.substring(AuthenticationConstants.TOKEN_PREFIX.length()).trim());
        if(!info.getRoles().contains("ADMIN") && !info.getUsername().equals(username)){
            throw new ForbiddenException();
        }
        return userMapper.userToDetailedUserDTO(userService.findOneByUserName(username));
    }

    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Save user")
    public DetailedUserDTO save(@RequestBody DetailedUserDTO detailedUserDTO){
        LOGGER.info("Saving a new user");
        User user = userMapper.detailedUserDTOToUser(detailedUserDTO);
        user = userService.save(user);
        return userMapper.userToDetailedUserDTO(user);
    }

    @RequestMapping(value = "/search/{keyword}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "find with keyword")
    public PaginationWrapper<SimpleUserDTO> findByKeyword(@PathVariable String keyword, Pageable pageable){
        LOGGER.info("Loading a user by filter");
        Page<User> page = userService.findByKeyword(keyword, pageable);
        return new PaginationWrapper<>(userMapper.userToSimpleUserDTO(page.getContent()), page.getTotalPages());
    }

    @RequestMapping(value = "/active/{username}/{active}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Set user active/deactive")
    public SimpleUserDTO setActive(@PathVariable String username, @PathVariable boolean active){
        LOGGER.info("Activate/Deactivate a user with name " + username);
        User user = userService.findOneByUserName(username);
        user = userService.setActive(user, active);
        return userMapper.userToSimpleUserDTO(user);
    }
}
