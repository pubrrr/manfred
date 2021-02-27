package manfred.manfreditor.gui;

import lombok.AllArgsConstructor;
import manfred.manfreditor.controller.GuiController;
import manfred.manfreditor.gui.view.MapView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

@Component
@AllArgsConstructor
public class GuiBuilder {

    private final GuiController guiController;
    private final MapView mapView;

    private final List<Consumer<String>> loadMapListeners = new LinkedList<>();

    public Gui build() {
        Display mainDisplay = new Display();
        Shell mainShell = new Shell(mainDisplay);
        mainShell.setText("Manfreditor");
        mainShell.setMinimumSize(mainShell.getSize());
        mainShell.setLayout(new RowLayout(SWT.VERTICAL));

        addControlButtons(mainShell);
        addMapCanvas(mainShell);

        return new Gui(mainShell, mainDisplay);
    }

    private void addControlButtons(Shell mainShell) {
        Composite composite = new Composite(mainShell, SWT.BORDER);
        composite.setLayout(new RowLayout(SWT.HORIZONTAL));

        Label label = new Label(composite, SWT.FILL);
        label.setText("Map debug");
        label.setLayoutData(new RowData(500, 30));
        label.addPaintListener(System.out::println);
        loadMapListeners.add(selectedFile -> {
            label.setText(selectedFile);
            label.redraw();
        });

        Button button = new Button(composite, SWT.CENTER);
        button.setText("Map laden");
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog fileDialog = new FileDialog(mainShell);
                String selectedFile = fileDialog.open();
                if (selectedFile != null) {
                    guiController.loadMap(selectedFile)
                        .onFailure(message -> {
                            MessageBox messageBox = new MessageBox(mainShell);
                            messageBox.setMessage(message);
                            messageBox.open();
                        });
                    loadMapListeners.forEach(stringConsumer -> stringConsumer.accept(selectedFile));
                }
            }
        });
    }

    private void addMapCanvas(Shell mainShell) {
        ScrolledComposite canvasContainer = new ScrolledComposite(mainShell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        canvasContainer.setLayoutData(new RowData(500, 500));
        canvasContainer.setLayout(new FillLayout());

        Canvas canvas = new Canvas(canvasContainer, SWT.BORDER);
        canvas.addPaintListener(event -> mapView.draw(event.gc, mainShell.getDisplay()));
        canvasContainer.setContent(canvas);
        loadMapListeners.add(selectedFile -> {
            canvas.setSize(mapView.getMapViewSize());
            canvas.redraw();
        });
    }

}
