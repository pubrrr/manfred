package manfred.manfreditor.gui;

import lombok.Data;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.util.Optional;

public class NewMapObjectDialog extends Dialog {

    private final ResultData resultData = new ResultData();
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<ResultData> result = Optional.empty();

    public NewMapObjectDialog(Shell parent) {
        super(parent);
    }

    public Optional<ResultData> open() {
        Shell parent = getParent();
        Shell shell = new Shell(parent);
        shell.setSize(450, 130);
        shell.setText("Neue Map");
        GridLayout layout = new GridLayout(3, false);
        shell.setLayout(layout);

        addYamlFileInput(shell);
        addImageFileInput(shell);

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

    private void addYamlFileInput(Shell shell) {
        Label yamlLabel = new Label(shell, 0);
        yamlLabel.setText("Yaml:");
        GridData yamlLabelLayout = new GridData(80, 20);
        yamlLabelLayout.verticalAlignment = SWT.BEGINNING;
        yamlLabel.setLayoutData(yamlLabelLayout);

        Text pathTextField = new Text(shell, SWT.BORDER);
        GridData pathTextFieldLayout = new GridData(SWT.BEGINNING, SWT.TOP, true, false);
        pathTextFieldLayout.widthHint = 300;
        pathTextField.setLayoutData(pathTextFieldLayout);
        pathTextField.addModifyListener(e -> this.resultData.yamlFilePath = pathTextField.getText());

        addBrowseButtonForTextField(pathTextField, shell);
    }

    private void addImageFileInput(Shell shell) {
        Label yamlLabel = new Label(shell, 0);
        yamlLabel.setText("Bild:");
        GridData yamlLabelLayout = new GridData(80, 20);
        yamlLabelLayout.verticalAlignment = SWT.BEGINNING;
        yamlLabel.setLayoutData(yamlLabelLayout);

        Text pathTextField = new Text(shell, SWT.BORDER);
        GridData pathTextFieldLayout = new GridData(SWT.BEGINNING, SWT.TOP, true, false);
        pathTextFieldLayout.widthHint = 300;
        pathTextField.setLayoutData(pathTextFieldLayout);
        pathTextField.addModifyListener(e -> this.resultData.imageFilePath = pathTextField.getText());

        addBrowseButtonForTextField(pathTextField, shell);
    }

    private void addBrowseButtonForTextField(Text textField, Shell shell) {
        Button browseButton = new Button(shell, SWT.CENTER);
        browseButton.setText("...");
        GridData browseButtonLayout = new GridData(20, 20);
        browseButtonLayout.verticalAlignment = SWT.END;
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
                    }
                }
            }
        );
    }

    private void addAbortButton(Shell shell) {
        Button button = new Button(shell, SWT.CENTER);
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
        Button button = new Button(shell, SWT.CENTER);
        button.setText("OK");
        button.addSelectionListener(
            new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    PopupProvider popupProvider = new PopupProvider();
                    if (NewMapObjectDialog.this.resultData.yamlFilePath.isEmpty()) {
                        popupProvider.showMessage(shell, "Yaml file path must not be empty");
                        return;
                    }
                    if (NewMapObjectDialog.this.resultData.imageFilePath.isEmpty()) {
                        popupProvider.showMessage(shell, "Image file path must not be empty");
                        return;
                    }
                    NewMapObjectDialog.this.result = Optional.of(NewMapObjectDialog.this.resultData);
                    shell.dispose();
                }
            }
        );
    }

    @Data
    public static class ResultData {

        private String yamlFilePath = "";
        private String imageFilePath = "";

        private ResultData() {
        }
    }
}
