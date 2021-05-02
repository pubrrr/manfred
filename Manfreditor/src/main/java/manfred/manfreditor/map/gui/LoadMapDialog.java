package manfred.manfreditor.map.gui;

import lombok.AllArgsConstructor;
import manfred.manfreditor.map.model.MapRepository;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.util.Comparator.comparing;
import static manfred.manfreditor.common.command.ControllerHelper.LEFT_MOUSE_BUTTON;

public class LoadMapDialog extends Dialog {

    private final io.vavr.collection.List<MapRepository.MapKey> sortedKeys;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<MapRepository.MapKey> result = Optional.empty();
    private List mapsList;

    public LoadMapDialog(Shell parent, io.vavr.collection.List<MapRepository.MapKey> sortedKeys) {
        super(parent);
        this.sortedKeys = sortedKeys;
    }

    public Optional<MapRepository.MapKey> open() {
        Shell parent = getParent();
        Shell shell = new Shell(parent);
        shell.setSize(280, 400);
        shell.setText("Neue Map");
        RowLayout layout = new RowLayout(SWT.HORIZONTAL);
        shell.setLayout(layout);

        mapsList = new List(shell, SWT.SINGLE);
        mapsList.addMouseListener(
            new MouseAdapter() {
                @Override
                public void mouseDoubleClick(MouseEvent e) {
                    if (e.button == LEFT_MOUSE_BUTTON) {
                        checkSelectionAndDispose(shell);
                    }
                }
            }
        );
        mapsList.setLayoutData(new RowData(260, 300));
        sortedKeys.forEach(key -> mapsList.add(key.value));

        Composite buttonContainer = new Composite(shell, 0);
        buttonContainer.setLayoutData(new RowData(260, 60));
        buttonContainer.setLayout(new GridLayout(2, false));
        addAbortButton(buttonContainer);
        addOkButton(buttonContainer);

        shell.open();
        Display display = parent.getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return this.result;
    }

    private void addAbortButton(Composite parent) {
        Button button = new Button(parent, SWT.CENTER);
        button.setLayoutData(new GridData(SWT.END, SWT.TOP, true, true));
        button.setText("Abbrechen");
        button.addSelectionListener(
            new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    LoadMapDialog.this.result = Optional.empty();
                    parent.dispose();
                }
            }
        );
    }

    private void addOkButton(Composite parent) {
        Button button = new Button(parent, SWT.CENTER);
        button.setLayoutData(new GridData(SWT.END, SWT.TOP, false, true));
        button.setText("OK");
        button.addSelectionListener(
            new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    checkSelectionAndDispose(parent);
                }
            }
        );
    }

    private void checkSelectionAndDispose(Composite parent) {
        int selectionIndex = mapsList.getSelectionIndex();
        if (selectionIndex >= 0) {
            LoadMapDialog.this.result = Optional.of(sortedKeys.get(selectionIndex));
            parent.getShell().dispose();
        }
    }

    @Component
    @AllArgsConstructor
    public static class Factory {

        private final MapRepository mapRepository;

        public LoadMapDialog create(Shell parent) {
            return new LoadMapDialog(parent, mapRepository.getKeys().toList().sorted(comparing(key -> key.value)));
        }
    }
}
