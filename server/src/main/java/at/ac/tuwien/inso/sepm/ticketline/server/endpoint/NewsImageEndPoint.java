package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.news.NewsImageDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.NewsImage;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.news.NewsImageMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.NewsImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/newsImage")
@Api(value = "newsImage")
public class NewsImageEndPoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewsImageEndPoint.class);

    private final NewsImageService newsImageService;
    private final NewsImageMapper newsImageMapper;

    public NewsImageEndPoint(NewsImageService newsImageService, NewsImageMapper newsImageMapper) {
        this.newsImageService = newsImageService;
        this.newsImageMapper = newsImageMapper;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get detailed information about a specific news entry")
    public NewsImageDTO find(@PathVariable Long id) {
        LOGGER.info("Loading an image by id " + id);
        return newsImageMapper.imageToImageDTO(newsImageService.findByOneId(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Create a news image entry")
    public NewsImageDTO createNewsImage(@RequestBody NewsImageDTO newsImageDTO) throws Exception {
        LOGGER.info("Publishing an image");
        NewsImage newsImage = newsImageMapper.imageDTOToImage(newsImageDTO);
        newsImage = newsImageService.createNewsImage(newsImage);
        return newsImageMapper.imageToImageDTO(newsImage);
    }
}
