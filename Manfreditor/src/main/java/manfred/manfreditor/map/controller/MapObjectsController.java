package manfred.manfreditor.map.controller;

import lombok.AllArgsConstructor;
import manfred.manfreditor.common.PopupProvider;
import manfred.manfreditor.common.command.ControllerHelper;
import manfred.manfreditor.map.controller.command.CreateMapObjectCommand;
import manfred.manfreditor.map.controller.command.SelectMapObjectCommand;
import manfred.manfreditor.newmapobject.NewMapObjectContext;
import manfred.manfreditor.newmapobject.gui.NewMapObjectDialog;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Shell;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

import static manfred.manfreditor.common.command.ControllerHelper.LEFT_MOUSE_BUTTON;

@Component
@AllArgsConstructor
public class MapObjectsController implements MouseListener {

    private final ControllerHelper controllerHelper;
    private final SelectMapObjectCommand.Factory selectMapObjectCommandFactory;
    private final CreateMapObjectCommand.Factory createMapObjectCommandFactory;
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

    public SelectionListener newObject(Shell mainShell) {
        return new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                createNewMapObjectDialogAndCreateObject(mainShell);
            }
        };
    }

    private void createNewMapObjectDialogAndCreateObject(Shell mainShell) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(NewMapObjectContext.class)) {
            NewMapObjectDialog newMapObjectDialog = context.getBean(NewMapObjectDialog.Factory.class).create(mainShell);
            newMapObjectDialog.open()
                .map(createMapObjectCommandFactory::create)
                .map(controllerHelper::execute)
                .ifPresent(commandResult -> commandResult.onFailure(
                    errorMessage -> popupProvider.showMessage(mainShell, errorMessage)
                ));
            postActions.forEach(Runnable::run);
        }
    }

    public void addPostAction(Runnable postAction) {
        this.postActions.add(postAction);
    }
}
