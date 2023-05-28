package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ClientServiceValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.client.util.LocalizationObserver;
import at.ac.tuwien.inso.sepm.ticketline.client.util.LocalizationSubject;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import org.controlsfx.glyphfont.FontAwesome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
public class NewsController implements LocalizationObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsController.class);

    @FXML
    public RadioButton rbUnreadNews;

    @FXML
    public RadioButton rbAllNews;

    @FXML
    private Button addButton;

    @FXML
    private VBox vbNewsElements;

    @FXML
    private TabHeaderController tabHeaderController;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private final NewsService newsService;
    private final UserService userService;

    private ObservableList<Node> vbNewsBoxChildren;

    private LocalizationSubject localizationSubject;

    public NewsController(MainController mainController, SpringFxmlLoader springFxmlLoader, NewsService newsService,
                          UserService userService, LocalizationSubject localizationSubject) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.newsService = newsService;
        this.userService = userService;
        this.localizationSubject = localizationSubject;
    }

    @FXML
    private void initialize() {
        LOGGER.info("News Controller initialized");
        tabHeaderController.setIcon(FontAwesome.Glyph.NEWSPAPER_ALT);
        tabHeaderController.setTitle("News");
        localizationSubject.attach(this);
        listener();
    }

    private void loadNews() {
        vbNewsBoxChildren = vbNewsElements.getChildren();
        vbNewsBoxChildren.clear();
        Task<List<SimpleNewsDTO>> task = new Task<>() {
            @Override
            protected List<SimpleNewsDTO> call() throws DataAccessException {
                return newsService.findAll();
            }

            @Override
            protected void succeeded() {
                LOGGER.info("Loading news successful");
                super.succeeded();
                for (Iterator<SimpleNewsDTO> iterator = getValue().iterator(); iterator.hasNext(); ) {
                    SimpleNewsDTO news = iterator.next();
                    SpringFxmlLoader.Wrapper<NewsElementController> wrapper =
                        springFxmlLoader.loadAndWrap("/fxml/news/newsElement.fxml");
                    wrapper.getController().initializeData(news, newsService);
                    vbNewsBoxChildren.add(wrapper.getLoadedObject());
                    if (iterator.hasNext()) {
                        Separator separator = new Separator();
                        vbNewsBoxChildren.add(separator);
                    }
                }
            }

            @Override
            protected void failed() {
                LOGGER.debug("Loading news failed.");
                super.failed();
                JavaFXUtils.createExceptionDialog(getException(),
                    vbNewsElements.getScene().getWindow()).showAndWait();
            }
        };
        task.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task).start();
    }

    public void onAddNewsButtonClicked() {
        LOGGER.info("Add button clicked.");
        SpringFxmlLoader.Wrapper<NewsAddController> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/news/newsAdd.fxml");
        Node node = mainController.newsTab.getContent();
        mainController.newsTab.setContent(wrapper.getLoadedObject());
        mainController.selectTab(mainController.newsTab);
        wrapper.getController().load(this, () -> {
            mainController.newsTab.setContent(node);
            mainController.selectTab(mainController.newsTab);
        });
        if(rbUnreadNews.isSelected()){
            loadUnreadNews();
        }else {
            loadNews();
        }
    }

    private void listener(){
        rbAllNews.selectedProperty().addListener(ov -> {
            if (rbAllNews.isSelected()) {
                loadNews();
            }else{
                loadUnreadNews();
            }
        });
    }

    private void setUserInterface() {
        addButton.setVisible(userService.isAdmin());
        addButton.setDisable(!userService.isAdmin());
    }

    void loadUnreadNews(){
        vbNewsBoxChildren = vbNewsElements.getChildren();
        vbNewsBoxChildren.clear();
        Task<List<SimpleNewsDTO>> task = new Task<>() {
            @Override
            protected List<SimpleNewsDTO> call() throws DataAccessException, ClientServiceValidationException {
                return newsService.findAllUnreadNews(userService.getCurrentUser().getId());
            }

            @Override
            protected void succeeded() {
                LOGGER.info("Loading unread news successful");
                super.succeeded();
                for (Iterator<SimpleNewsDTO> iterator = getValue().iterator(); iterator.hasNext(); ) {
                    SimpleNewsDTO news = iterator.next();
                    SpringFxmlLoader.Wrapper<NewsElementController> wrapper =
                        springFxmlLoader.loadAndWrap("/fxml/news/newsElement.fxml");
                    wrapper.getController().initializeData(news, newsService);
                    wrapper.getController().setNode(wrapper.getLoadedObject());
                    vbNewsBoxChildren.add(wrapper.getLoadedObject());
                    if (iterator.hasNext()) {
                        Separator separator = new Separator();
                        vbNewsBoxChildren.add(separator);
                        wrapper.getController().setSeparator(separator);
                    }
                }
            }

            @Override
            protected void failed() {
                LOGGER.debug("Loading news failed.");
                super.failed();
                JavaFXUtils.createExceptionDialog(getException(),
                    vbNewsElements.getScene().getWindow()).showAndWait();
            }
        };
        task.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task).start();
    }

    public void onTabOpen() {
        rbUnreadNews.setSelected(true);
        loadUnreadNews();
        setUserInterface();
    }

    public void onTabClose(){

    }

    void removeElement(Node node, Separator separator){
        if (node != null) {
            vbNewsBoxChildren.remove(node);
        }
        if (separator != null) {
            vbNewsBoxChildren.remove(separator);
        }
    }

    @Override
    public void update() {
        rbAllNews.setText(BundleManager.getBundle().getString("news.all"));
        rbUnreadNews.setText(BundleManager.getBundle().getString("news.unread"));
        addButton.setText(BundleManager.getBundle().getString("button.news.add"));
    }
}
