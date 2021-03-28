package manfred.manfreditor.controller;

import lombok.AllArgsConstructor;
import manfred.manfreditor.controller.command.CommandResult;
import manfred.manfreditor.controller.command.DeleteMapObjectCommand;
import manfred.manfreditor.controller.command.InsertMapObjectCommand;
import manfred.manfreditor.controller.command.LoadMapCommand;
import manfred.manfreditor.controller.command.NewMapCommand;
import manfred.manfreditor.controller.command.SaveMapCommand;
import manfred.manfreditor.gui.NewMapDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static manfred.manfreditor.controller.ControllerHelper.LEFT_MOUSE_BUTTON;
import static manfred.manfreditor.controller.ControllerHelper.RIGHT_MOUSE_BUTTON;

@Component
@AllArgsConstructor
public class MapController implements MouseListener {

    private final ControllerHelper controllerHelper;
    private final LoadMapCommand.Factory loadMapCommandFactory;
    private final SaveMapCommand.Factory saveMapCommandFactory;
    private final NewMapCommand.Factory newMapCommandFactory;
    private final InsertMapObjectCommand.Factory insertMapObjectCommandFactory;
    private final DeleteMapObjectCommand.Factory deleteMapObjectCommandFactory;

    private final List<Consumer<String>> loadMapPostActions;
    private final List<Runnable> insertPostActions;
    private final List<Runnable> deletePostActions;

    public CommandResult loadMap(String selectedFile) {
        return controllerHelper.execute(loadMapCommandFactory.create(selectedFile));
    }

    public SelectionListener loadMap(Shell mainShell) {
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
                    loadMapPostActions.forEach(stringConsumer -> stringConsumer.accept(selectedFile));
                }
            }
        };
    }

    public SelectionListener saveMap(Shell mainShell) {
        return new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog fileDialog = new FileDialog(mainShell);
                String selectedFile = fileDialog.open();
                if (selectedFile != null) {
                    File fileToSaveIn = new File(selectedFile);
                    if (fileToSaveIn.isFile()) {
                        MessageBox messageBox = new MessageBox(mainShell, SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
                        messageBox.setMessage("Obacht:\n\n" + selectedFile + "\ngibts scho. Willsch trotzdem?");
                        int open = messageBox.open();
                        if (open == SWT.CANCEL) {
                            return;
                        }
                    }
                    controllerHelper.execute(saveMapCommandFactory.create(fileToSaveIn, mainShell))
                        .onFailure(errorMessage -> {
                            MessageBox messageBox = new MessageBox(mainShell, SWT.ICON_ERROR | SWT.OK);
                            messageBox.setMessage("Des hod id fongtsionierd:\n\n" + errorMessage);
                            messageBox.open();
                        });
                }
            }
        };
    }

    public SelectionListener createNewMap(Shell mainShell) {
        return new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                NewMapDialog newMapDialog = new NewMapDialog(mainShell);
                Optional<NewMapDialog.ResultData> result = newMapDialog.open();
                result.ifPresent(executeCreateNewMapCommand(mainShell));
            }
        };
    }

    private Consumer<NewMapDialog.ResultData> executeCreateNewMapCommand(Shell mainShell) {
        return resultData -> {
            controllerHelper
                .execute(newMapCommandFactory.create(resultData.getName(), resultData.getColumns(), resultData.getRows()))
                .onFailure(errorMessage -> {
                    MessageBox messageBox = new MessageBox(mainShell, SWT.ICON_ERROR | SWT.OK);
                    messageBox.setMessage("Des hod id fongtsionierd:\n\n" + errorMessage);
                    messageBox.open();
                });
            loadMapPostActions.forEach(stringConsumer -> stringConsumer.accept("new map"));
        };
    }

    public void addLoadMapPostAction(Consumer<String> selectedFileConsumer) {
        this.loadMapPostActions.add(selectedFileConsumer);
    }

    public void addInsertPostAction(Runnable postAction) {
        this.insertPostActions.add(postAction);
    }

    public void addDeletePostAction(Runnable postAction) {
        this.deletePostActions.add(postAction);
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
            controllerHelper.execute(insertMapObjectCommandFactory.create(event.x, event.y)).onFailure(System.out::println);
            insertPostActions.forEach(Runnable::run);
        } else if (event.button == RIGHT_MOUSE_BUTTON) {
            controllerHelper.execute(deleteMapObjectCommandFactory.create(event.x, event.y)).onFailure(System.out::println);
            deletePostActions.forEach(Runnable::run);
        }
    }
}
