package manfred.manfreditor;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ManfreditorController {
    public Button button;
    public Pane mapPane;
    public ScrollPane mapPaneContainer;

    private int xMax = 0;
    private int yMax = 0;

    public void onClick(ActionEvent actionEvent) {
        xMax++;
        yMax++;
        mapPane.setMinWidth(xMax * 50);
        mapPane.setMinHeight(yMax * 20);

        for (int x = 0; x < xMax; x++) {
            for (int y = 0; y < yMax; y++) {

                Rectangle rectangle = new Rectangle(x * 50, y * 20, 50, 20);
                rectangle.setFill((x + y) % 2 == 0 ? Color.RED : Color.BLACK);
                mapPane.getChildren().add(rectangle);
            }
        }
        mapPaneContainer.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        mapPaneContainer.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        mapPaneContainer.setPannable(true);
    }

    public void drag(ScrollEvent mouseEvent) {
    }
}
