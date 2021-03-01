package manfred.manfreditor.controller;

import lombok.AllArgsConstructor;
import manfred.manfreditor.controller.command.CommandResult;
import manfred.manfreditor.controller.command.InsertMapObjectCommand;
import manfred.manfreditor.controller.command.LoadMapCommand;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

import static manfred.manfreditor.controller.ControllerHelper.LEFT_MOUSE_BUTTON;
import static manfred.manfreditor.controller.ControllerHelper.execute;

@Component
@AllArgsConstructor
public class MapController implements MouseListener {

    private final LoadMapCommand.Factory loadMapCommandFactory;
    private final InsertMapObjectCommand.Factory insertMapObjectCommandFactory;
    private final List<Consumer<String>> postActions;

    public CommandResult loadMap(String selectedFile) {
        return execute(loadMapCommandFactory.create(selectedFile));
    }

    public SelectionListener withShell(Shell mainShell) {
        return new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog fileDialog = new FileDialog(mainShell);
                String selectedFile = fileDialog.open();
                if (selectedFile != null) {
                    loadMap(selectedFile).onFailure(message -> {
                            MessageBox messageBox = new MessageBox(mainShell);
                            messageBox.setMessage(message);
                            messageBox.open();
                        });
                    postActions.forEach(stringConsumer -> stringConsumer.accept(selectedFile));
                }
            }
        };
    }

    public void addPostAction(Consumer<String> selectedFileConsumer) {
        this.postActions.add(selectedFileConsumer);
    }

    @Override
    public void mouseDoubleClick(MouseEvent e) {
    }

    @Override
    public void mouseDown(MouseEvent e) {
    }

    @Override
    public void mouseUp(MouseEvent event) {
        if (event.button == LEFT_MOUSE_BUTTON) {
            execute(insertMapObjectCommandFactory.create(event.x, event.y)).onFailure(System.out::println);
        }
    }
}
