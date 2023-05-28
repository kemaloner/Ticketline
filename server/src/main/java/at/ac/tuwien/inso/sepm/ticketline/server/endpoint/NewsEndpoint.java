package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationTokenInfo;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.news.NewsMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.security.AuthenticationConstants;
import at.ac.tuwien.inso.sepm.ticketline.server.service.HeaderTokenAuthenticationService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping(value = "/news")
@Api(value = "news")
public class NewsEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewsEndpoint.class);

    private final UserService userService;
    private final NewsService newsService;
    private final HeaderTokenAuthenticationService authenticationService;
    private final NewsMapper newsMapper;

    public NewsEndpoint(UserService userService, NewsService newsService, HeaderTokenAuthenticationService authenticationService, NewsMapper newsMapper) {
        this.userService = userService;
        this.newsService = newsService;
        this.authenticationService = authenticationService;
        this.newsMapper = newsMapper;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get list of simple news entries")
    public List<SimpleNewsDTO> findAll() {
        LOGGER.info("Loading all news");
        return newsMapper.newsToSimpleNewsDTO(newsService.findAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get detailed information about a specific news entry")
    public DetailedNewsDTO find(@PathVariable Long id) {
        LOGGER.info("Finding detailed news");
        return newsMapper.newsToDetailedNewsDTO(newsService.findOne(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Publish a new news entry")
    public DetailedNewsDTO publishNews(@RequestBody DetailedNewsDTO detailedNewsDTO) {
        LOGGER.info("Publishing news");
        News news = newsMapper.detailedNewsDTOToNews(detailedNewsDTO);
        news = newsService.publishNews(news);
        return newsMapper.newsToDetailedNewsDTO(news);
    }

    @RequestMapping(value = "/unreadnews/{userId}", method = RequestMethod.GET)
    @ApiOperation(value = "Get a list of news which are not read by the user yet")
    public List<SimpleNewsDTO> findUnreadNews(@PathVariable("userId") Long userId){
        LOGGER.info("Loading all unread news");
        return newsMapper.newsToSimpleNewsDTO(newsService.findUnreadNews(userId));
    }

    @RequestMapping(value = "/read/{newsId}", method = RequestMethod.POST)
    @ApiOperation(value = "Mark a news 'read' for the user")
    public void readNews(@ApiIgnore @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorizationHeader, @PathVariable("newsId") Long newsId){
        LOGGER.info("Setting news as read for a curtain user");
        AuthenticationTokenInfo info = authenticationService.authenticationTokenInfo(authorizationHeader.substring(AuthenticationConstants.TOKEN_PREFIX.length()).trim());
        User user = userService.findOneByUserName(info.getUsername());

        News news = newsService.findOne(newsId);
        userService.readNews(user.getId(), news);
    }

}
