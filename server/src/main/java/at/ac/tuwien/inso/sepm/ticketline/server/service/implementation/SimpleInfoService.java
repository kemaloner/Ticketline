package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.info.Info;
import at.ac.tuwien.inso.sepm.ticketline.server.endpoint.contributor.InfoEndpointUptimeContributor;
import at.ac.tuwien.inso.sepm.ticketline.server.service.InfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.info.GitProperties;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Service
public class SimpleInfoService implements InfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleInfoService.class);

    private GitProperties gitProperties;
    private static final DateTimeFormatter ISO_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private InfoEndpointUptimeContributor infoEndpointUptimeContributor;

    public SimpleInfoService(GitProperties gitProperties, InfoEndpointUptimeContributor infoEndpointUptimeContributor){
        this.gitProperties = gitProperties;
        this.infoEndpointUptimeContributor = infoEndpointUptimeContributor;
    }

    @Override
    public Info find() {
        LOGGER.info("Requesting info");
        Info info = new Info();
        Info.Git git = new Info.Git();
        Info.Git.Commit commit = new Info.Git.Commit();
        Info.Git.Build build = new Info.Git.Build();
        Info.Git.Commit.Id id = new Info.Git.Commit.Id();

        id.setAbbrev(gitProperties.get("commit.id.abbrev"));

        commit.setId(id);
        commit.setTime(ZonedDateTime.parse(gitProperties.get("commit.time"), ISO_DATETIME_FORMATTER));

        build.setTime(ZonedDateTime.parse(gitProperties.get("build.time"), ISO_DATETIME_FORMATTER));
        build.setVersion(gitProperties.get("build.version"));

        git.setBranch(gitProperties.getBranch());
        git.setCommit(commit);
        git.setBuild(build);
        git.setTags(gitProperties.get("tags"));

        info.setGit(git);
        info.setUptime(Duration.of(infoEndpointUptimeContributor.getRuntimeMXBean().getUptime(), ChronoUnit.MILLIS));
        return info;
    }
}
