package manfred.manfreditor.controller;

import lombok.AllArgsConstructor;
import manfred.manfreditor.controller.command.LoadMapObjectCommand;
import manfred.manfreditor.controller.command.SelectMapObjectCommand;
import manfred.manfreditor.gui.NewMapObjectDialog;
import manfred.manfreditor.gui.PopupProvider;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Shell;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

import static manfred.manfreditor.controller.ControllerHelper.LEFT_MOUSE_BUTTON;

@Component
@AllArgsConstructor
public class MapObjectsController implements MouseListener {

    private final ControllerHelper controllerHelper;
    private final SelectMapObjectCommand.Factory selectMapObjectCommandFactory;
    private final LoadMapObjectCommand.Factory loadMapObjectCommandFactory;
    private final List<Runnable> postActions = new LinkedList<>();
    private final PopupProvider popupProvider;

    @Override
    public void mouseDoubleClick(MouseEvent e) {
    }

    @Override
    public void mouseDown(MouseEvent e) {
    }

    @Override
    public void mouseUp(MouseEvent event) {
        if (event.button == LEFT_MOUSE_BUTTON) {
            controllerHelper.execute(selectMapObjectCommandFactory.create(event.x, event.y));
            postActions.forEach(Runnable::run);
        }
    }

    public SelectionListener loadObject(Shell mainShell) {
        return new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                var newMapObjectDialog = new NewMapObjectDialog(mainShell);
                newMapObjectDialog.open()
                    .map(resultData -> loadMapObjectCommandFactory.create(
                        resultData.getYamlFilePath(),
                        resultData.getImageFilePath()
                    ))
                    .map(controllerHelper::execute)
                    .ifPresent(commandResult -> commandResult.onFailure(
                        errorMessage -> popupProvider.showMessage(mainShell, errorMessage)
                    ));
                postActions.forEach(Runnable::run);
            }
        };
    }

    public void addPostAction(Runnable postAction) {
        this.postActions.add(postAction);
    }
}
