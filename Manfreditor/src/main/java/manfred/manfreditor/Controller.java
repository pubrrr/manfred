package manfred.manfreditor;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Controller {
    public Button button;
    public GridPane mapPane;
    public ScrollPane mapPaneContainer;

    private int xMax = 0;
    private int yMax = 0;

//    private final MapReader mapReader;

//    public ManfreditorController(MapReader mapReader) {
//        this.mapReader = mapReader;
//    }

    public void onClick(ActionEvent actionEvent) {
        Node gridLines = mapPane.getChildren().get(0);
        mapPane.getChildren().clear();
        mapPane.getChildren().add(0, gridLines);

        xMax++;
        yMax++;
        mapPane.setMinWidth(xMax * 50);
        mapPane.setMinHeight(yMax * 50);

        for (int x = 0; x < xMax; x++) {
            for (int y = 0; y < yMax; y++) {

                Rectangle rectangle = new Rectangle(x * 50, y * 50, 50, 50);
                rectangle.setFill((((x + y) % 2) == 0) ? new Color(1, 0, 0, 0.4) : new Color(1, 1, 1, 0.4));
                mapPane.add(rectangle, x, y);
            }
        }
        mapPaneContainer.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        mapPaneContainer.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        mapPaneContainer.setPannable(true);
    }

    public void drag(ScrollEvent mouseEvent) {
    }
}
