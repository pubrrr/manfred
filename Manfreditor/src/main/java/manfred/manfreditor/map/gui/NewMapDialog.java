package manfred.manfreditor.map.gui;

import lombok.Data;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import java.util.Optional;

public class NewMapDialog extends Dialog {

    private final ResultData resultData = new ResultData();
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<ResultData> result = Optional.empty();

    public NewMapDialog(Shell parent) {
        super(parent);
    }

    public Optional<ResultData> open() {
        Shell parent = getParent();
        Shell shell = new Shell(parent);
        shell.setSize(280, 160);
        shell.setText("Neue Map");
        GridLayout layout = new GridLayout(2, false);
        shell.setLayout(layout);

        addNameTextField(shell);
        addRowsInputField(shell);
        addColumnsInputField(shell);

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
        GridData nameLabelLayout = new GridData(100, 20);
        nameLabelLayout.verticalAlignment = SWT.BEGINNING;
        nameLabel.setLayoutData(nameLabelLayout);

        Text nameField = new Text(shell, SWT.BORDER);
        GridData nameFieldLayout = new GridData(100, 20);
        nameFieldLayout.verticalAlignment = SWT.END;
        nameField.setLayoutData(nameFieldLayout);
        nameField.addModifyListener(e -> this.resultData.name = nameField.getText());
    }

    private void addRowsInputField(Shell shell) {
        Label rowsLabel = new Label(shell, 0);
        rowsLabel.setText("Zeilen:");
        GridData rowsLabelLayout = new GridData(100, 20);
        rowsLabelLayout.verticalAlignment = SWT.BEGINNING;
        rowsLabel.setLayoutData(rowsLabelLayout);

        Spinner rowsSpinner = new Spinner(shell, 0);
        GridData rowsSpinnerLayout = new GridData(100, 20);
        rowsSpinnerLayout.verticalAlignment = SWT.END;
        rowsSpinner.setLayoutData(rowsSpinnerLayout);
        rowsSpinner.setMinimum(1);
        rowsSpinner.setMaximum(100);
        rowsSpinner.addModifyListener(e -> this.resultData.rows = rowsSpinner.getSelection());
    }

    private void addColumnsInputField(Shell shell) {
        Label columnsLabel = new Label(shell, 0);
        columnsLabel.setText("Spalten:");
        GridData columnsLabelLayout = new GridData(100, 20);
        columnsLabelLayout.verticalAlignment = SWT.BEGINNING;
        columnsLabel.setLayoutData(columnsLabelLayout);

        Spinner columnsSpinner = new Spinner(shell, 0);
        GridData columnsSpinnerLayout = new GridData(100, 20);
        columnsSpinnerLayout.verticalAlignment = SWT.END;
        columnsSpinner.setLayoutData(columnsSpinnerLayout);
        columnsSpinner.setMinimum(1);
        columnsSpinner.setMaximum(100);
        columnsSpinner.addModifyListener(e -> this.resultData.columns = columnsSpinner.getSelection());
    }

    private void addAbortButton(Shell shell) {
        Button button = new Button(shell, SWT.CENTER);
        button.setText("Abbrechen");
        button.addSelectionListener(
            new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    NewMapDialog.this.result = Optional.empty();
                    shell.dispose();
                }
            }
        );
    }

    private void addOkButton(Shell shell) {
        Button button = new Button(shell, SWT.CENTER);
        button.setText("OK");
        button.addSelectionListener(
            new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    NewMapDialog.this.result = Optional.of(NewMapDialog.this.resultData);
                    shell.dispose();
                }
            }
        );
    }

    @Data
    public static class ResultData {

        private String name = "neue Map";
        private int rows = 1;
        private int columns = 1;

        private ResultData() {
        }
    }
}
