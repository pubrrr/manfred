package manfred.manfreditor.controller;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import lombok.AllArgsConstructor;
import manfred.manfreditor.controller.newmapobject.ClickObjectPreviewCommand;
import manfred.manfreditor.controller.newmapobject.LoadObjectImageCommand;
import manfred.manfreditor.mapobject.NewMapObjectData;
import manfred.manfreditor.mapobject.NewMapObjectModel;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;

import static manfred.manfreditor.controller.ControllerHelper.LEFT_MOUSE_BUTTON;

@Component
@AllArgsConstructor
public class NewMapObjectController {

    private final NewMapObjectModel newMapObjectModel;
    private final ControllerHelper controllerHelper;
    private final LoadObjectImageCommand.Factory loadObjectImageCommandFactory;
    private final ClickObjectPreviewCommand.Factory clickObjectPreviewCommandFactory;

    private final List<Runnable> postActions;

    public void newSession() {
        newMapObjectModel.newSession();
    }

    public ModifyListener setName(String name) {
        return e -> newMapObjectModel.setName(name);
    }

    public SelectionListener setImageFromPath(Supplier<String> imagePathSupplier) {
        return new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                controllerHelper.execute(loadObjectImageCommandFactory.create(imagePathSupplier.get()));
                postActions.forEach(Runnable::run);
            }
        };
    }

    public MouseListener clickOnObjectPreview(Supplier<Point> canvasSizeSupplier) {
        return new MouseAdapter() {

            @Override
            public void mouseUp(MouseEvent event) {
                if (event.button == LEFT_MOUSE_BUTTON) {
                    controllerHelper.execute(clickObjectPreviewCommandFactory.create(event.x, event.y, canvasSizeSupplier.get()));
                    postActions.forEach(Runnable::run);
                }
            }
        };
    }

    public Validation<Seq<String>, NewMapObjectData> getResult() {
        return newMapObjectModel.getResult();
    }

    public void addPostAction(Runnable postAction) {
        this.postActions.add(postAction);
    }
}
