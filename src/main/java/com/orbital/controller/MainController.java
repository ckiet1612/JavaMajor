package com.orbital.controller;

import com.orbital.model.SpaceObject;
import com.orbital.repository.SpaceObjectRepository;
import com.orbital.service.PhysicsService;
import com.orbital.service.RoutingService;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class MainController implements Initializable {

    private static final double SIMULATION_TIME_SCALE = 1000.0;

    @FXML private StackPane sceneContainer;
    @FXML private VBox floatingInfoBox;
    @FXML private TextField nameField;
    @FXML private TextField latField;
    @FXML private TextField lonField;
    @FXML private TextField altField;
    @FXML private TextField incField;
    @FXML private TextField sizeField;
    @FXML private ColorPicker colorPicker;
    @FXML private ComboBox<SpaceObject> fromCombo;
    @FXML private ComboBox<SpaceObject> toCombo;
    @FXML private Label statusLabel;
    @FXML private Label detailLabel;
    @FXML private ListView<SpaceObject> objectListView;

    private final SpaceObjectRepository repository;
    private final PhysicsService physicsService;
    private final RoutingService routingService;

    private OrbitSceneManager sceneManager;
    private boolean routingActive;
    private List<SpaceObject> currentRoute = new ArrayList<>();

    private enum StatusType {
        OK,
        WARN,
        ERR,
        LINK
    }

    public MainController(SpaceObjectRepository repository, PhysicsService physicsService, RoutingService routingService) {
        this.repository = repository;
        this.physicsService = physicsService;
        this.routingService = routingService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colorPicker.setValue(SpaceObjectUiFormatter.DEFAULT_ASSET_COLOR);

        sceneManager = new OrbitSceneManager(physicsService, sceneContainer, this::clearSceneSelection);
        sceneManager.initialize();

        loadData();
        setupListSelectionBehavior();
        setStatus(StatusType.OK, "Hệ thống sẵn sàng. " + objectListView.getItems().size() + " vệ tinh đang theo dõi.");
        startAnimation();
    }

    @FXML
    public void handleAddObject() {
        try {
            String name = nameField.getText() == null ? "" : nameField.getText().trim();
            if (name.isEmpty()) {
                setStatus(StatusType.WARN, "Tên vệ tinh không được để trống.");
                return;
            }

            boolean exists = objectListView.getItems().stream()
                    .anyMatch(object -> object.getName().equalsIgnoreCase(name));
            if (exists) {
                setStatus(StatusType.WARN, "Callsign '" + name + "' đã tồn tại trong mạng.");
                return;
            }

            double lat = Double.parseDouble(latField.getText());
            double lon = Double.parseDouble(lonField.getText());
            double alt = Double.parseDouble(altField.getText());
            double inc = Double.parseDouble(incField.getText());

            SpaceObject object = new SpaceObject(name, "SATELLITE", lat, lon, alt, inc);
            if (sizeField.getText() != null && !sizeField.getText().isEmpty()) {
                object.setSize(Double.parseDouble(sizeField.getText()));
            }
            if (colorPicker.getValue() != null) {
                // JavaFX gives 0xRRGGBBAA, but the database only needs RGB.
                object.setColorHex(SpaceObjectUiFormatter.toHex(colorPicker.getValue()));
            }

            double velocity = physicsService.calculateOrbitalVelocity(alt);
            object.setVelocity(velocity);

            repository.save(object);
            sceneManager.addObject(object);

            objectListView.getItems().add(object);
            fromCombo.getItems().add(object);
            toCombo.getItems().add(object);

            setStatus(
                    StatusType.OK,
                    "Đã phóng " + name + " vào " + SpaceObjectUiFormatter.formatOrbitBand(alt)
                            + " với vận tốc " + String.format("%.2f", velocity) + " km/s."
            );
        } catch (Exception e) {
            setStatus(StatusType.ERR, "Không thể phóng vệ tinh: " + e.getMessage());
        }
    }

    @FXML
    public void handleRoute() {
        SpaceObject from = fromCombo.getValue();
        SpaceObject to = toCombo.getValue();

        if (from == null || to == null) {
            setStatus(StatusType.WARN, "Chọn đủ điểm phát và điểm thu để mở liên kết.");
            return;
        }

        if (from.equals(to)) {
            setStatus(StatusType.WARN, "Điểm phát và điểm thu cần là hai vệ tinh khác nhau.");
            return;
        }

        routingActive = true;
        setStatus(StatusType.LINK, "Đang mở liên kết tín hiệu từ " + from.getName() + " đến " + to.getName() + ".");
    }

    @FXML
    public void handleClearRoute() {
        routingActive = false;
        currentRoute.clear();
        sceneManager.clearRouteLines();
        setStatus(StatusType.OK, "Đã ngắt liên kết tín hiệu.");
    }

    @FXML
    public void handleDeleteSelected() {
        List<SpaceObject> selected = new ArrayList<>(objectListView.getSelectionModel().getSelectedItems());
        if (selected.isEmpty()) {
            setStatus(StatusType.WARN, "Chọn ít nhất một vệ tinh trong mạng theo dõi.");
            return;
        }

        repository.deleteAll(selected);
        for (SpaceObject object : selected) {
            sceneManager.removeObject(object);
            objectListView.getItems().remove(object);
            fromCombo.getItems().remove(object);
            toCombo.getItems().remove(object);
        }

        if (!currentRoute.isEmpty() && !Collections.disjoint(currentRoute, selected)) {
            handleClearRoute();
        }

        setStatus(StatusType.OK, "Đã loại " + selected.size() + " vệ tinh khỏi mạng theo dõi.");
    }

    @FXML
    public void handleDeleteAll() {
        if (objectListView.getItems().isEmpty()) {
            setStatus(StatusType.WARN, "Mạng theo dõi hiện không có vệ tinh.");
            return;
        }

        repository.deleteAll();
        sceneManager.clearAllObjects();
        objectListView.getItems().clear();
        fromCombo.getItems().clear();
        toCombo.getItems().clear();

        handleClearRoute();
        setStatus(StatusType.OK, "Đã xóa toàn bộ mạng vệ tinh.");
    }

    private void loadData() {
        List<SpaceObject> objects = repository.findAll();
        objectListView.setItems(FXCollections.observableArrayList(objects));
        fromCombo.setItems(FXCollections.observableArrayList(objects));
        toCombo.setItems(FXCollections.observableArrayList(objects));

        objectListView.setCellFactory(param -> SpaceObjectCellFactory.createAssetListCell());
        fromCombo.setCellFactory(param -> SpaceObjectCellFactory.createCompactAssetCell());
        toCombo.setCellFactory(param -> SpaceObjectCellFactory.createCompactAssetCell());
        fromCombo.setButtonCell(SpaceObjectCellFactory.createCompactAssetCell());
        toCombo.setButtonCell(SpaceObjectCellFactory.createCompactAssetCell());

        for (SpaceObject object : objects) {
            sceneManager.addObject(object);
        }
    }

    private void setupListSelectionBehavior() {
        objectListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        objectListView.getSelectionModel().getSelectedItems().addListener(
                (javafx.collections.ListChangeListener.Change<? extends SpaceObject> change) -> updateSelectionDetails()
        );
        objectListView.addEventFilter(MouseEvent.MOUSE_PRESSED, this::handleObjectListMousePressed);
    }

    private void handleObjectListMousePressed(MouseEvent event) {
        Node node = event.getPickResult().getIntersectedNode();
        if (isScrollBarClick(node)) {
            return;
        }

        ListCell<?> cell = findListCell(node);
        if (cell == null) {
            objectListView.getSelectionModel().clearSelection();
            return;
        }

        if (cell.isEmpty()) {
            objectListView.getSelectionModel().clearSelection();
            event.consume();
            return;
        }

        int index = cell.getIndex();
        if (objectListView.getSelectionModel().isSelected(index)) {
            objectListView.getSelectionModel().clearSelection(index);
            event.consume();
        }
    }

    private boolean isScrollBarClick(Node node) {
        while (node != null && node != objectListView) {
            if (node instanceof ScrollBar) {
                return true;
            }
            node = node.getParent();
        }
        return false;
    }

    private ListCell<?> findListCell(Node node) {
        while (node != null && node != objectListView) {
            if (node instanceof ListCell<?> cell) {
                return cell;
            }
            node = node.getParent();
        }
        return null;
    }

    private void updateSelectionDetails() {
        ObservableList<SpaceObject> selected = objectListView.getSelectionModel().getSelectedItems();
        sceneManager.showHighlights(selected);

        if (selected.size() == 1) {
            detailLabel.setText(SpaceObjectUiFormatter.formatSatelliteDetails(selected.get(0)));
            floatingInfoBox.setVisible(true);
        } else {
            floatingInfoBox.setVisible(false);
        }
    }

    private void clearSceneSelection() {
        objectListView.getSelectionModel().clearSelection();
        floatingInfoBox.setVisible(false);
    }

    private void startAnimation() {
        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate;

            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }

                double elapsedSeconds = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;

                advanceSimulation(elapsedSeconds);
                updateRoutingState();
                sceneManager.updateRouteLines(currentRoute);
            }
        };
        timer.start();
    }

    private void advanceSimulation(double elapsedSeconds) {
        // Speed up the orbit so movement is visible in a short demo.
        for (SpaceObject object : objectListView.getItems()) {
            double angularVelocity = physicsService.calculateAngularVelocity(object.getVelocity(), object.getAltitude());
            object.setCurrentAngle(object.getCurrentAngle() + angularVelocity * elapsedSeconds * SIMULATION_TIME_SCALE);
        }
        sceneManager.updateObjectPositions(objectListView.getItems());
    }

    private void updateRoutingState() {
        if (!routingActive) {
            return;
        }

        SpaceObject from = fromCombo.getValue();
        SpaceObject to = toCombo.getValue();
        if (from == null || to == null || !objectListView.getItems().contains(from) || !objectListView.getItems().contains(to)) {
            routingActive = false;
            currentRoute.clear();
            setStatus(StatusType.WARN, "Liên kết tín hiệu bị ngắt.");
            return;
        }

        currentRoute = routingService.findShortestPath(from, to, objectListView.getItems());
        if (currentRoute.isEmpty()) {
            setStatus(StatusType.ERR, "Liên kết bị che khuất, mất tín hiệu.");
        } else {
            setStatus(StatusType.LINK, "Đang theo dõi tín hiệu qua " + currentRoute.size() + " trạm.");
        }
    }

    private void setStatus(StatusType type, String message) {
        statusLabel.setText(message);
        statusLabel.getStyleClass().removeAll("status-ok", "status-warn", "status-err", "status-link");
        statusLabel.getStyleClass().add(statusClass(type));
    }

    private String statusClass(StatusType type) {
        return switch (type) {
            case WARN -> "status-warn";
            case ERR -> "status-err";
            case LINK -> "status-link";
            case OK -> "status-ok";
        };
    }
}
