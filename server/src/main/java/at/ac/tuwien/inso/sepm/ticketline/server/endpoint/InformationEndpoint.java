package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.info.Info;
import at.ac.tuwien.inso.sepm.ticketline.server.endpoint.contributor.InfoEndpointUptimeContributor;
import at.ac.tuwien.inso.sepm.ticketline.server.service.InfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/information")
@Api(value = "information")
public class InformationEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(InformationEndpoint.class);


    private InfoService infoService;

    public InformationEndpoint(InfoService infoService, InfoEndpointUptimeContributor infoEndpointUptimeContributor){
        this.infoService = infoService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get meta info on server")
    public Info find(){
        LOGGER.info("Loading server information");
        return infoService.find();
    }
}
