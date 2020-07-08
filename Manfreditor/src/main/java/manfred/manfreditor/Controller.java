package manfred.manfreditor;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import manfred.manfreditor.exception.InvalidInputException;
import manfred.manfreditor.map.Map;
import manfred.manfreditor.map.MapPane;
import manfred.manfreditor.map.MapReader;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@FxmlView("manfreditor.fxml")
public class Controller {
    private static final Color notAccessibleColor = new Color(1, 0, 0, 0.4);
    private static final Color accessibleColor = new Color(0, 0, 0, 0.0);

    public Button loadMapButton;
    public MapPane mapPane;
    public ScrollPane mapPaneContainer;

    private final MapReader mapReader;
    private Map map;

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
            map = mapReader.load(mapName);

            int horizontalSize = map.getArray().length;
            int verticalSize = map.getArray()[0].length;
            mapPane.resetWithSize(horizontalSize, verticalSize);

            for (int x = 0; x < horizontalSize; x++) {
                for (int y = 0; y < verticalSize; y++) {
                    mapPane.addTile(
                        createMapTile(map.getArray()[x][y]),
                        x,
                        y
                    );
                }
            }
        } catch (InvalidInputException | IOException e) {
            showErrorMessagePopup(e.getMessage());
        }
    }

    private Rectangle createMapTile(boolean isAccessible) {
        Rectangle rectangle = new Rectangle(MapPane.PIXEL_BLOCK_SIZE, MapPane.PIXEL_BLOCK_SIZE);
        rectangle.setFill(
            isAccessible
                ? accessibleColor
                : notAccessibleColor
        );
        rectangle.setOnMouseClicked(this::onGridPaneClicked);
        return rectangle;
    }

    private void showErrorMessagePopup(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler!!1!!1!1");
        alert.setHeaderText("Map nicht gefunden");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void onGridPaneClicked(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        int x = GridPane.getColumnIndex(source);
        int y = GridPane.getRowIndex(source);

        map.getArray()[x][y] = !map.getArray()[x][y];
        mapPane.getTile(x, y).setFill(
            map.getArray()[x][y]
                ? accessibleColor
                : notAccessibleColor
        );
    }
}
