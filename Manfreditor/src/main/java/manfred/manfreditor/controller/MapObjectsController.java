package manfred.manfreditor.controller;

import lombok.AllArgsConstructor;
import manfred.manfreditor.controller.command.SelectMapObjectCommand;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

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

    public void addPostAction(Runnable postAction) {
        this.postActions.add(postAction);
    }
}
