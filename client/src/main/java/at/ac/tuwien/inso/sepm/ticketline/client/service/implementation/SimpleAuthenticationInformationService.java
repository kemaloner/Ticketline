package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.service.AuthenticationInformationService;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationTokenInfo;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SimpleAuthenticationInformationService implements AuthenticationInformationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleAuthenticationInformationService.class);

    private String currentAuthenticationToken;

    private AuthenticationTokenInfo currentAuthenticationTokenInfo;
    private List<AuthenticationChangeListener> changeListener = new ArrayList<>();

    @Override
    public void setCurrentAuthenticationToken(String currentAuthenticationToken) {
        LOGGER.info("Setting current authentication token");
        this.currentAuthenticationToken = currentAuthenticationToken;
    }

    @Override
    public void setCurrentAuthenticationTokenInfo(AuthenticationTokenInfo currentAuthenticationTokenInfo) {
        LOGGER.info("Setting current authentication token info");
        this.currentAuthenticationTokenInfo = currentAuthenticationTokenInfo;
        changeListener.forEach(authenticationChangeListener ->
            Platform.runLater(() -> authenticationChangeListener.changed(this.currentAuthenticationTokenInfo)
            ));
    }

    @Override
    public Optional<String> getCurrentAuthenticationToken() {
        LOGGER.info("Getting current authentication token");
        return Optional.ofNullable(currentAuthenticationToken);
    }

    @Override
    public Optional<AuthenticationTokenInfo> getCurrentAuthenticationTokenInfo() {
        LOGGER.info("Getting current authentication token info");
        return Optional.ofNullable(currentAuthenticationTokenInfo);
    }

    @Override
    public void clearAuthentication() {
        LOGGER.info("Removing authentication");
        currentAuthenticationToken = null;
        currentAuthenticationTokenInfo = null;
        changeListener.forEach(authenticationChangeListener ->
            authenticationChangeListener.changed(currentAuthenticationTokenInfo));
    }

    @Override
    public void addAuthenticationChangeListener(AuthenticationChangeListener changeListener) {
        this.changeListener.add(changeListener);
    }


}
