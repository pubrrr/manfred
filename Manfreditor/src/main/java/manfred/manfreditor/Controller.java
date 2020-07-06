package manfred.manfreditor;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import manfred.manfreditor.exception.InvalidInputException;
import manfred.manfreditor.map.Map;
import manfred.manfreditor.map.MapReader;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@FxmlView("manfreditor.fxml")
public class Controller {
    public static final int PIXEL_BLOCK_SIZE = 50;

    public Button loadMapButton;
    public GridPane mapPane;
    public ScrollPane mapPaneContainer;

    private final MapReader mapReader;

    public Controller(MapReader mapReader) {
        this.mapReader = mapReader;
    }

    public void loadMapDialogue(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Lade Map");
        dialog.setHeaderText("Was willsch denn jetzt scho wieder?");
        dialog.setContentText("Name der Map:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(this::loadMap);
    }

    private void loadMap(String mapName) {
        try {
            Map map = mapReader.load(mapName);

            int horizontalSize = map.getArray().length;
            int verticalSize = map.getArray()[0].length;

            clearMapPane();
            mapPane.setMinWidth(horizontalSize * PIXEL_BLOCK_SIZE);
            mapPane.setMinHeight(verticalSize * PIXEL_BLOCK_SIZE);

            for (int x = 0; x < horizontalSize; x++) {
                for (int y = 0; y < verticalSize; y++) {
                    if (!map.isAccessible(x, y)) {
                        Rectangle rectangle = new Rectangle(x * 50, y * 50, 50, 50);
                        rectangle.setFill(new Color(1, 0, 0, 0.4));
                        mapPane.add(rectangle, x, y);
                    }
                }
            }
        } catch (InvalidInputException | IOException e) {
            showErrorMessagePopup(e.getMessage());
        }
    }

    private void showErrorMessagePopup(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler!!1!!1!1");
        alert.setHeaderText("Map nicht gefunden");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearMapPane() {
        Node gridLines = mapPane.getChildren().get(0);
        mapPane.getChildren().clear();
        mapPane.getChildren().add(0, gridLines);
    }
}
