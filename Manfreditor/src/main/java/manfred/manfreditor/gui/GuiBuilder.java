package manfred.manfreditor.gui;

import lombok.AllArgsConstructor;
import manfred.manfreditor.controller.GuiController;
import manfred.manfreditor.map.MapView;
import org.eclipse.swt.SWT;
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

@Component
@AllArgsConstructor
public class GuiBuilder {

    private final GuiController guiController;
    private final MapView mapView;

    public Gui build() {
        Display mainDisplay = new Display();
        Shell mainShell = new Shell(mainDisplay);
        mainShell.setText("Manfreditor");
        mainShell.setMinimumSize(mainShell.getSize());
        mainShell.setLayout(new RowLayout(SWT.VERTICAL));

        Composite composite = new Composite(mainShell, SWT.BORDER);
        composite.setLayout(new RowLayout(SWT.HORIZONTAL));

        Label label = new Label(composite, SWT.FILL);
        label.setText("Map debug");
        label.setLayoutData(new RowData(500, 30));
        label.addPaintListener(System.out::println);

        Button button = new Button(composite, SWT.CENTER);
        button.setText("Map laden");

        Composite composite1 = new Composite(mainShell, SWT.BORDER);
        composite1.setLayoutData(new RowData(500, 500));

        composite1.setLayout(new FillLayout());

//        Label label2 = new Label(composite, SWT.FILL);
//        label2.setText("blablub");

        Canvas canvas = new Canvas(composite1, SWT.BORDER);
        canvas.setSize(500, 100);
        canvas.addPaintListener(event -> {
            mapView.draw(event.gc, mainShell.getDisplay());
        });

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
                    label.setText(selectedFile);
                    label.redraw();
                    canvas.redraw();
                }
            }
        });

        return new Gui(mainShell, mainDisplay);
    }

}
