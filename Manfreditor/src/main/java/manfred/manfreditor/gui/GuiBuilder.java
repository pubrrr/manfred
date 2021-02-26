package manfred.manfreditor.gui;

import lombok.AllArgsConstructor;
import manfred.manfreditor.controller.GuiController;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
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

    public Gui build() {
        Display mainDisplay = new Display();
        Shell mainShell = new Shell(mainDisplay);
        mainShell.setText("Manfreditor");
        mainShell.setMinimumSize(mainShell.getSize());
        mainShell.setLayout(new RowLayout(SWT.VERTICAL));

        addControlButtonsRow(mainShell);

        Composite composite2 = new Composite(mainShell, SWT.BORDER);
        Label label2 = new Label(composite2, SWT.FILL);
        label2.setText("blablub");

        return new Gui(mainShell, mainDisplay);
    }

    private void addControlButtonsRow(Shell mainShell) {
        Composite composite1 = new Composite(mainShell, SWT.BORDER);
        composite1.setLayout(new RowLayout(SWT.HORIZONTAL));

        Label label = new Label(composite1, SWT.FILL);
        label.setText("Map debug");
        label.setLayoutData(new RowData(500, 30));
        label.addPaintListener(System.out::println);

        Button button = new Button(composite1, SWT.CENTER);
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
                    label.setText(selectedFile);
                    System.out.println(label.getText());
                    label.redraw();
                }
            }
        });
    }
}
