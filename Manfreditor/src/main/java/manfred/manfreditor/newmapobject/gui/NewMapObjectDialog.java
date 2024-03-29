package manfred.manfreditor.newmapobject.gui;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import lombok.AllArgsConstructor;
import manfred.manfreditor.common.PopupProvider;
import manfred.manfreditor.newmapobject.controller.NewMapObjectController;
import manfred.manfreditor.newmapobject.view.NewMapObjectView;
import manfred.manfreditor.newmapobject.model.NewMapObjectData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static manfred.manfreditor.common.command.ControllerHelper.KEY_ENTER;

public class NewMapObjectDialog extends Dialog {

    private final NewMapObjectController newMapObjectController;
    private final NewMapObjectView newMapObjectView;

    private Button okButton;
    private Spinner columnsSpinner;
    private Spinner rowsSpinner;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<NewMapObjectData> result = Optional.empty();

    private NewMapObjectDialog(Shell parent, NewMapObjectController newMapObjectController, NewMapObjectView newMapObjectView) {
        super(parent);
        this.newMapObjectController = newMapObjectController;
        this.newMapObjectView = newMapObjectView;
    }

    public Optional<NewMapObjectData> open() {
        Shell parent = getParent();
        Shell shell = new Shell(parent);
        shell.setSize(500, 680);
        shell.setText("Neue Map");
        GridLayout layout = new GridLayout(3, false);
        shell.setLayout(layout);

        addNameTextField(shell);
        addImageFileInput(shell);
        addRowAndColumnSpinner(shell);

        addObjectCanvas(shell);

        addAbortButton(shell);
        addOkButton(shell);

        shell.open();
        Display display = parent.getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return this.result;
    }

    private void addNameTextField(Shell shell) {
        Label nameLabel = new Label(shell, 0);
        nameLabel.setText("Name:");
        GridData nameLabelLayout = new GridData(SWT.TOP, SWT.BEGINNING, true, false);
        nameLabelLayout.verticalAlignment = SWT.BEGINNING;
        nameLabel.setLayoutData(nameLabelLayout);

        Text nameField = new Text(shell, SWT.BORDER);
        GridData nameFieldLayout = new GridData(SWT.TOP, SWT.END, true, false, 1, 1);
        nameFieldLayout.widthHint = 400;
        nameField.setLayoutData(nameFieldLayout);
        nameField.addModifyListener(newMapObjectController.setName(nameField::getText));

        new Composite(shell, 0); // filler
    }

    private void addImageFileInput(Shell shell) {
        Label imageLabel = new Label(shell, 0);
        imageLabel.setText("Bild:");
        GridData imageLabelLayout = new GridData(80, 20);
        imageLabelLayout.verticalAlignment = SWT.BEGINNING;
        imageLabel.setLayoutData(imageLabelLayout);

        Text imagePathTextField = new Text(shell, SWT.BORDER);
        GridData imagePathTextFieldLayout = new GridData(SWT.BEGINNING, SWT.TOP, true, false);
        imagePathTextFieldLayout.widthHint = 400;
        imagePathTextField.setLayoutData(imagePathTextFieldLayout);
        imagePathTextField.addKeyListener(
            new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent event) {
                    if (event.keyCode == KEY_ENTER) {
                        newMapObjectController.setImageFromPath(imagePathTextField::getText);
                    }
                }
            }
        );

        addBrowseButtonForTextField(imagePathTextField, shell);
    }

    private void addBrowseButtonForTextField(Text textField, Shell shell) {
        Button browseButton = new Button(shell, SWT.CENTER);
        browseButton.setText("...");
        GridData browseButtonLayout = new GridData(20, 20);
        browseButtonLayout.verticalAlignment = SWT.TOP;
        browseButtonLayout.horizontalAlignment = SWT.BEGINNING;
        browseButton.setLayoutData(browseButtonLayout);
        browseButton.addSelectionListener(
            new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    FileDialog fileDialog = new FileDialog(shell);
                    String selectedFile = fileDialog.open();
                    if (selectedFile != null) {
                        textField.setText(selectedFile);
                        textField.redraw();
                        okButton.setEnabled(true);
                        rowsSpinner.setEnabled(true);
                        columnsSpinner.setEnabled(true);
                        newMapObjectController.setImageFromPath(textField::getText);
                    }
                }
            }
        );
    }

    private void addRowAndColumnSpinner(Shell shell) {
        Label columnsLabel = new Label(shell, 0);
        columnsLabel.setText("Spalten (x):");
        GridData columnsLabelLayout = new GridData(80, 20);
        columnsLabelLayout.verticalAlignment = SWT.BEGINNING;
        columnsLabel.setLayoutData(columnsLabelLayout);

        columnsSpinner = new Spinner(shell, 0);
        GridData columnsSpinnerLayout = new GridData(SWT.BEGINNING, SWT.TOP, true, false);
        columnsSpinnerLayout.horizontalSpan = 2;
        columnsSpinner.setLayoutData(columnsSpinnerLayout);
        columnsSpinner.setEnabled(false);
        columnsSpinner.setMinimum(1);
        columnsSpinner.setMaximum(20);
        columnsSpinner.addModifyListener(e -> newMapObjectController.setColumns(columnsSpinner::getSelection));

        Label rowsLabel = new Label(shell, 0);
        rowsLabel.setText("Zeilen (y):");
        GridData rowsLabelLayout = new GridData(80, 20);
        rowsLabelLayout.verticalAlignment = SWT.BEGINNING;
        rowsLabel.setLayoutData(rowsLabelLayout);

        rowsSpinner = new Spinner(shell, 0);
        GridData rowsSpinnerLayout = new GridData(SWT.BEGINNING, SWT.TOP, true, false);
        rowsSpinnerLayout.horizontalSpan = 2;
        rowsSpinner.setLayoutData(rowsSpinnerLayout);
        rowsSpinner.setEnabled(false);
        rowsSpinner.setMinimum(1);
        rowsSpinner.setMaximum(20);
        rowsSpinner.addModifyListener(e -> newMapObjectController.setRows(rowsSpinner::getSelection));
    }

    private void addObjectCanvas(Shell shell) {
        Canvas objectPreviewCanvas = new Canvas(shell, SWT.BORDER);
        GridData layoutData = new GridData(SWT.BEGINNING, SWT.TOP, true, true, 3, 1);
        layoutData.widthHint = 1000;
        layoutData.heightHint = 480;
        objectPreviewCanvas.setLayoutData(layoutData);
        objectPreviewCanvas.addMouseListener(newMapObjectController.clickOnObjectPreview(objectPreviewCanvas::getSize));
        objectPreviewCanvas.addPaintListener(event -> newMapObjectView.draw(event.gc, shell.getDisplay(), objectPreviewCanvas.getSize()));
        newMapObjectController.addPostAction(objectPreviewCanvas::redraw);
    }

    private void addAbortButton(Shell shell) {
        Button button = new Button(shell, SWT.CENTER);
        button.setLayoutData(new GridData(SWT.END, SWT.BOTTOM, false, true, 2, 1));
        button.setText("Abbrechen");
        button.addSelectionListener(
            new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    NewMapObjectDialog.this.result = Optional.empty();
                    shell.dispose();
                }
            }
        );
    }

    private void addOkButton(Shell shell) {
        okButton = new Button(shell, SWT.CENTER);
        okButton.setLayoutData(new GridData(SWT.END, SWT.BOTTOM, false, true));
        okButton.setText("OK");
        okButton.setEnabled(false);
        okButton.addSelectionListener(
            new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    Validation<Seq<String>, NewMapObjectData> errorOrResult = newMapObjectController.getResult();
                    errorOrResult
                        .toEither()
                        .peekLeft(errorMessages -> new PopupProvider().showMessage(shell, String.join(",\n", errorMessages)))
                        .peek(newMapObjectData -> {
                            NewMapObjectDialog.this.result = Optional.of(newMapObjectData);
                            shell.dispose();
                        });
                }
            }
        );
    }

    @Component
    @AllArgsConstructor
    public static class Factory {

        private final NewMapObjectController newMapObjectController;
        private final NewMapObjectView newMapObjectView;

        public NewMapObjectDialog create(Shell shell) {
            newMapObjectController.newSession();
            return new NewMapObjectDialog(shell, newMapObjectController, newMapObjectView);
        }
    }
}
