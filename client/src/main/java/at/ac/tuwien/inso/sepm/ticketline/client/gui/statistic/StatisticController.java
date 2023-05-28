package at.ac.tuwien.inso.sepm.ticketline.client.gui.statistic;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.PerformanceController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.client.util.LocalizationObserver;
import at.ac.tuwien.inso.sepm.ticketline.client.util.LocalizationSubject;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.Top10EventDTOImpl;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import org.controlsfx.glyphfont.FontAwesome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager.text;

@Component
public class StatisticController implements LocalizationObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticController.class);

    @FXML
    private TabHeaderController tabHeaderController;

    @FXML
    private VBox vbEventsElements;

    @FXML
    private ChoiceBox<String> cbTyp;

    @FXML
    private ChoiceBox<String> cbMonth;

    @FXML
    private ChoiceBox<Integer> cbYear;

    @FXML
    private BarChart<String, Integer> bcStatistic;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private final ChangeListener changeListener = (ChangeListener<Integer>) (observable, oldValue, newValue) -> loadTop10();

    private EventService eventService;

    private MainController mainController;

    private LocalizationSubject localizationSubject;

    private final SpringFxmlLoader springFxmlLoader;

    private Map<XYChart.Data, Long> eventIDs = new HashMap<>();

    public StatisticController(SpringFxmlLoader springFxmlLoader, EventService eventService,
                               MainController mainController, LocalizationSubject localizationSubject){
        this.springFxmlLoader = springFxmlLoader;
        this.eventService = eventService;
        this.mainController = mainController;
        this.localizationSubject = localizationSubject;
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.BAR_CHART);
        tabHeaderController.setTitle(text("event.statistic"));
        localizationSubject.attach(this);

        xAxis.setLabel("Events");
        yAxis.setLabel("Count");
        bcStatistic.setAnimated(false);

        localizationSubject.attach(this);
        setChoiceBox();
        startListener();
        }

    public void onTabOpen(){
        loadTop10();
    }

    public void onTabClose(){

    }

    private void loadTop10(){
        bcStatistic.getData().clear();
        eventIDs.clear();
        Task<List<Top10EventDTOImpl>> task = new Task<>() {
            @Override
            protected List<Top10EventDTOImpl> call() throws Exception {
                MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                if (0 != cbTyp.getSelectionModel().getSelectedIndex()) {
                    if(1 == cbTyp.getSelectionModel().getSelectedIndex()){
                        params.add("category", "CONCERT");
                    }else if(2 == cbTyp.getSelectionModel().getSelectedIndex()){
                        params.add("category", "CINEMA");
                    }else if(3 == cbTyp.getSelectionModel().getSelectedIndex()){
                        params.add("category", "FESTIVAL");
                    }else if(4 == cbTyp.getSelectionModel().getSelectedIndex()){
                        params.add("category", "THEATRE");
                    }else if(5 == cbTyp.getSelectionModel().getSelectedIndex()){
                        params.add("category", "MUSICAL");
                    }else{
                        params.add("category", "OPERA");
                    }

                }
                LocalDateTime startDate = LocalDateTime.of(Integer.parseInt(cbYear.getValue().toString()), Integer.parseInt(cbMonth.getValue()), 1, 0, 0);
                params.add("startDate", String.valueOf(startDate));
                return eventService.findTop10Event(params);
            }

            @Override
            protected void succeeded() {
                LOGGER.info("Loading event successful.");
                super.succeeded();
                XYChart.Series<String, Integer> seriesBarChart = new XYChart.Series<>();
                int i = 1;
                for (Top10EventDTOImpl topEvent : getValue()){
                    XYChart.Data<String, Integer> xyChart = new XYChart.Data<>(i++ + "-" + topEvent.getTitle(), topEvent.getCount());
                    eventIDs.put(xyChart, topEvent.getId());
                    seriesBarChart.getData().add(xyChart);
                }
                bcStatistic.getData().add(seriesBarChart);
                for (final XYChart.Data<String, Integer> column : seriesBarChart.getData()) {
                    final Node bar = column.getNode();
                    bar.setEffect(null);
                    bar.setOnMouseClicked(e -> {
                        bar.getStyleClass().add("selected");
                        if(eventIDs.containsKey(column)){
                            goToPerformance(eventIDs.get(column));
                        }
                    });
                }
            }

            @Override
            protected void failed() {
                super.failed();
                JavaFXUtils.createExceptionDialog(getException(),vbEventsElements.getScene().getWindow()).showAndWait();
            }
        };
        task.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task).start();
    }

    private void goToPerformance(Long eventID){
        Task<EventDTO> task = new Task<>() {
            @Override
            protected EventDTO call() throws Exception {
                return eventService.findOneById(eventID);
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                SpringFxmlLoader.Wrapper<PerformanceController> wrapper = springFxmlLoader.loadAndWrap("/fxml/events/performanceComponent.fxml");
                wrapper.getController().setData(getValue(),mainController.eventsTab);
                mainController.eventsTab.setContent(wrapper.getLoadedObject());
                mainController.selectTab(mainController.eventsTab);
            }

            @Override
            protected void failed() {
                LOGGER.debug("Loading event failed.");
                super.failed();
                JavaFXUtils.createExceptionDialog(getException(),
                    vbEventsElements.getScene().getWindow()).showAndWait();
            }
        };
        task.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task).start();
    }

    private void setChoiceBox(){
        ObservableList observableList = FXCollections.observableArrayList();
        for (int i = 1; i <= 12; i ++){
            observableList.add(String.format("%02d",i));
        }
        cbMonth.setItems(observableList);
        cbMonth.setValue(String.format("%02d",LocalDateTime.now().getMonthValue()));


        observableList = FXCollections.observableArrayList();
        for (int i = 2017; i <= LocalDateTime.now().getYear()+1; i ++){
            observableList.add(i);
        }
        cbYear.setItems(observableList);
        cbYear.setValue(LocalDateTime.now().getYear());

        observableList = FXCollections.observableArrayList(BundleManager.getBundle().getString("statistic.allCategories"), BundleManager.getBundle().getString("statistic.concert"), BundleManager.getBundle().getString("statistic.cinema"), BundleManager.getBundle().getString("statistic.festival"), BundleManager.getBundle().getString("statistic.theatre"), BundleManager.getBundle().getString("statistic.musical"), BundleManager.getBundle().getString("statistic.opera"));
        cbTyp.setItems(observableList);
        cbTyp.getSelectionModel().selectFirst();
    }

    private void setTypeChoiceBox(){
        int temp = cbTyp.getSelectionModel().getSelectedIndex();
        ObservableList observableList = FXCollections.observableArrayList(BundleManager.getBundle().getString("statistic.allCategories"),
            BundleManager.getBundle().getString("statistic.concert"), BundleManager.getBundle().getString("statistic.cinema"),
            BundleManager.getBundle().getString("statistic.festival"), BundleManager.getBundle().getString("statistic.theatre"),
            BundleManager.getBundle().getString("statistic.musical"), BundleManager.getBundle().getString("statistic.opera"));
        cbTyp.setItems(observableList);
        cbTyp.getSelectionModel().select(temp);
    }



    private void stopListener(){
        cbMonth.getSelectionModel().selectedIndexProperty().removeListener(changeListener);
        cbYear.getSelectionModel().selectedIndexProperty().removeListener(changeListener);
        cbTyp.getSelectionModel().selectedIndexProperty().removeListener(changeListener);
    }

    private void startListener(){
        cbMonth.getSelectionModel().selectedIndexProperty().addListener(changeListener);
        cbYear.getSelectionModel().selectedIndexProperty().addListener(changeListener);
        cbTyp.getSelectionModel().selectedIndexProperty().addListener(changeListener);
    }

    @Override
    public void update() {
        tabHeaderController.setTitle(text("event.statistic"));
        stopListener();
        setTypeChoiceBox();
        startListener();
    }
}
