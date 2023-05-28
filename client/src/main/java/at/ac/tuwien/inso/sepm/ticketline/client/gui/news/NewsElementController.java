package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.client.TicketlineClientApplication;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ClientServiceValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.client.util.LocalizationObserver;
import at.ac.tuwien.inso.sepm.ticketline.client.util.LocalizationSubject;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class NewsElementController implements LocalizationObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsElementController.class);

    private static final DateTimeFormatter NEWS_DTF =
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT);

    @Autowired
    private LocalizationSubject localizationSubject;

    @Autowired
    private UserService userService;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblText;

    @FXML
    private Button btNewsDetails;

    private Node node;
    private Separator separator;

    private SimpleNewsDTO simpleNewsDTO;
    private NewsService newsService;
    private SpringFxmlLoader springFxmlLoader;

    @Autowired
    private NewsController newsController;

    public NewsElementController(SpringFxmlLoader springFxmlLoader){
        this.springFxmlLoader = springFxmlLoader;
    }

    public void initializeData(SimpleNewsDTO simpleNewsDTO, NewsService newsService) {
        lblDate.setText(NEWS_DTF.format(simpleNewsDTO.getPublishedAt()));
        lblTitle.setText(simpleNewsDTO.getTitle());
        lblText.setText(simpleNewsDTO.getSummary());
        btNewsDetails.setText(BundleManager.getBundle().getString("button.news.details"));
        this.simpleNewsDTO = simpleNewsDTO;
        this.newsService = newsService;
        this.localizationSubject.attach(this);
    }

    public void onViewDetailClicked(ActionEvent event) {
        LOGGER.info("Detailed view button is clicked.");
        Task<DetailedNewsDTO> task = new Task<>() {
            @Override
            protected DetailedNewsDTO call() throws DataAccessException, ClientServiceValidationException {
                LOGGER.info("Loading detailed news.");
                DetailedNewsDTO detailedNewsDTO = newsService.findOneById(simpleNewsDTO.getId());
                newsService.readNews(detailedNewsDTO.getId());
                return detailedNewsDTO;
            }
            @Override
            protected void succeeded() {
                LOGGER.info("Loading detailed news is success.");
                super.succeeded();
                Stage stage = new Stage();
                SpringFxmlLoader.Wrapper<NewsDetailController> wrapper = springFxmlLoader.loadAndWrap("/fxml/news/newsDetail.fxml");
                NewsDetailController newsDetailController = wrapper.getController();
                newsDetailController.setDetailedNewsDTO(getValue());
                newsController.removeElement(node,separator);
                Parent root = (Parent) wrapper.getLoadedObject();
                stage.initStyle(StageStyle.DECORATED);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.getIcons().add(new Image(TicketlineClientApplication.class.getResourceAsStream("/image/ticketlineIcon.png")));
                stage.show();
                stage.setResizable(false);
            }
            @Override
            protected void failed() {
                LOGGER.info("Loading detailed news is failed.");
                super.failed();
                JavaFXUtils.createExceptionDialog(getException(), lblDate.getScene().getWindow()).showAndWait();
            }
        };
        new Thread(task).start();
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public void setSeparator(Separator separator) {
        this.separator = separator;
    }

    @Override
    public void update() {
        btNewsDetails.setText(BundleManager.getBundle().getString("button.news.details"));
    }
}
