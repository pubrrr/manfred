package manfred.manfreditor.controller;

import lombok.AllArgsConstructor;
import manfred.manfreditor.controller.command.SelectMapObjectCommand;
import manfred.manfreditor.gui.NewMapObjectDialog;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Shell;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static manfred.manfreditor.controller.ControllerHelper.LEFT_MOUSE_BUTTON;

@Component
@AllArgsConstructor
public class MapObjectsController implements MouseListener {

    private final ControllerHelper controllerHelper;
    private final SelectMapObjectCommand.Factory selectMapObjectCommandFactory;
    private final List<Runnable> postActions = new LinkedList<>();

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
                Optional<NewMapObjectDialog.ResultData> result = newMapObjectDialog.open();
                System.out.println(result);
            }
        };
    }

    public void addPostAction(Runnable postAction) {
        this.postActions.add(postAction);
    }
}
