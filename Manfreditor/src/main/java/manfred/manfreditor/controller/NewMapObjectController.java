package manfred.manfreditor.controller;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import lombok.AllArgsConstructor;
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
import org.springframework.stereotype.Component;

import static io.vavr.API.TODO;

@Component
@AllArgsConstructor
public class NewMapObjectController {

    private final NewMapObjectModel newMapObjectModel;
    private final ControllerHelper controllerHelper;
    private final LoadObjectImageCommand.Factory loadObjectImageCommandFactory;

    public void newSession() {
        newMapObjectModel.newSession();
    }

    public ModifyListener setName(String name) {
        return e -> newMapObjectModel.setName(name);
    }

    public SelectionListener setImageFromPath(String imagePath) {
        return new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                controllerHelper.execute(loadObjectImageCommandFactory.create(imagePath));
            }
        };
    }

    public MouseListener clickOnObjectPreview() {
        return new MouseAdapter() {

            @Override
            public void mouseUp(MouseEvent e) {
                TODO();
            }
        };
    }

    public Validation<Seq<String>, NewMapObjectData> getResult() {
        return newMapObjectModel.getResult();
    }
}
