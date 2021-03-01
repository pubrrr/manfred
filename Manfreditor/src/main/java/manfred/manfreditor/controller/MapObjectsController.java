package manfred.manfreditor.controller;

import lombok.AllArgsConstructor;
import manfred.manfreditor.controller.command.SelectMapObjectCommand;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.springframework.stereotype.Component;

import static manfred.manfreditor.controller.ControllerHelper.LEFT_MOUSE_BUTTON;
import static manfred.manfreditor.controller.ControllerHelper.execute;

@Component
@AllArgsConstructor
public class MapObjectsController implements MouseListener {

    private final SelectMapObjectCommand.Factory selectMapObjectCommandFactory;

    @Override
    public void mouseDoubleClick(MouseEvent e) {
    }

    @Override
    public void mouseDown(MouseEvent e) {
    }

    @Override
    public void mouseUp(MouseEvent event) {
        if (event.button == LEFT_MOUSE_BUTTON) {
            execute(selectMapObjectCommandFactory.create(event.x, event.y));
        }
    }
}
