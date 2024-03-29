package manfred.manfreditor.newmapobject.controller;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import lombok.AllArgsConstructor;
import manfred.manfreditor.common.command.ControllerHelper;
import manfred.manfreditor.newmapobject.controller.command.ClickObjectPreviewCommand;
import manfred.manfreditor.newmapobject.controller.command.LoadObjectImageCommand;
import manfred.manfreditor.newmapobject.controller.command.SetColumnsCommand;
import manfred.manfreditor.newmapobject.controller.command.SetRowsCommand;
import manfred.manfreditor.newmapobject.model.NewMapObjectData;
import manfred.manfreditor.newmapobject.model.NewMapObjectModel;
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

import static manfred.manfreditor.common.command.ControllerHelper.LEFT_MOUSE_BUTTON;

@Component
@AllArgsConstructor
public class NewMapObjectController {

    private final NewMapObjectModel newMapObjectModel;
    private final ControllerHelper controllerHelper;
    private final LoadObjectImageCommand.Factory loadObjectImageCommandFactory;
    private final ClickObjectPreviewCommand.Factory clickObjectPreviewCommandFactory;
    private final SetColumnsCommand.Factory setColumnsCommandFactory;
    private final SetRowsCommand.Factory setRowsCommandFactory;

    private final List<Runnable> postActions;

    public void newSession() {
        this.postActions.clear();
        newMapObjectModel.newSession();
    }

    public ModifyListener setName(Supplier<String> nameSupplier) {
        return e -> newMapObjectModel.setName(nameSupplier.get());
    }

    public void setImageFromPath(Supplier<String> imagePathSupplier) {
        controllerHelper.execute(loadObjectImageCommandFactory.create(imagePathSupplier.get()));
        postActions.forEach(Runnable::run);
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

    public void setColumns(Supplier<Integer> columnsSupplier) {
        controllerHelper.execute(setColumnsCommandFactory.create(columnsSupplier.get()));
        postActions.forEach(Runnable::run);
    }

    public void setRows(Supplier<Integer> rowsSupplier) {
        controllerHelper.execute(setRowsCommandFactory.create(rowsSupplier.get()));
        postActions.forEach(Runnable::run);
    }
}
