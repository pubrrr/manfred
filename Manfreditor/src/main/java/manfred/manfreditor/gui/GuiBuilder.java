package manfred.manfreditor.gui;

import lombok.AllArgsConstructor;
import manfred.manfreditor.controller.MapController;
import manfred.manfreditor.controller.MapObjectsController;
import manfred.manfreditor.controller.RollbackController;
import manfred.manfreditor.gui.view.map.MapView;
import manfred.manfreditor.gui.view.mapobject.MapObjectsView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GuiBuilder {

    private final MapObjectsController mapObjectsController;
    private final MapController mapController;
    private final RollbackController rollbackController;

    private final MapView mapView;
    private final MapObjectsView mapObjectsView;

    public Gui build() {
        Display mainDisplay = new Display();
        Shell mainShell = new Shell(mainDisplay);
        mainShell.setText("Manfreditor");
        mainShell.setLayout(new GridLayout(1, false));

        addControlButtons(mainShell);
        addMapAndMapObjects(mainShell);

        mainShell.layout();
        mainShell.setMinimumSize(mainShell.getSize());
        return new Gui(mainShell, mainDisplay);
    }

    private void addControlButtons(Shell mainShell) {
        Composite composite = new Composite(mainShell, SWT.BORDER);
        composite.setLayout(new RowLayout(SWT.HORIZONTAL));

        Label label = new Label(composite, SWT.FILL);
        label.setText("Map debug");
        label.setLayoutData(new RowData(500, 30));
        mapController.addLoadMapPostAction(selectedFile -> {
            label.setText(selectedFile);
            label.redraw();
        });

        Button newMapObjectButton = new Button(composite, SWT.CENTER);
        newMapObjectButton.setText("Neues Objekt");
        newMapObjectButton.addSelectionListener(mapObjectsController.newObject(mainShell));

        Button newMapButton = new Button(composite, SWT.CENTER);
        newMapButton.setText("Neue Map");
        newMapButton.addSelectionListener(mapController.createNewMap(mainShell));

        Button loadMapButton = new Button(composite, SWT.CENTER);
        loadMapButton.setText("Map laden");
        loadMapButton.addSelectionListener(mapController.loadMap(mainShell));

        Button saveMapButton = new Button(composite, SWT.CENTER);
        saveMapButton.setText("Map speichern");
        saveMapButton.addSelectionListener(mapController.saveMap(mainShell));

        Button rollbackButton = new Button(composite, SWT.CENTER);
        rollbackButton.setText("Rückgängig");
        rollbackButton.addSelectionListener(rollbackController);
    }

    private void addMapAndMapObjects(Shell mainShell) {
        Composite mapAndMapObjectsContainer = new Composite(mainShell, SWT.BORDER);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        mapAndMapObjectsContainer.setLayoutData(gridData);
        mapAndMapObjectsContainer.setLayout(new GridLayout(2, false));

        addMapCanvas(mapAndMapObjectsContainer, mainShell);
        addMapObjectsCanvas(mapAndMapObjectsContainer, mainShell);
    }

    private void addMapCanvas(Composite mapAndMapObjectsContainer, Shell mainShell) {
        ScrolledComposite mapScrollContainer = new ScrolledComposite(mapAndMapObjectsContainer, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        mapScrollContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        mapScrollContainer.setLayout(new FillLayout());

        Canvas mapCanvas = new Canvas(mapScrollContainer, SWT.NONE);
        mapScrollContainer.setContent(mapCanvas); // to make scrolling work
        mapCanvas.addMouseListener(mapController);
        mapCanvas.addPaintListener(event -> mapView.draw(event.gc, mainShell.getDisplay()));
        mapController.addLoadMapPostAction(selectedFile -> {
            mapCanvas.setSize(mapView.getMapViewSize());
            mapCanvas.redraw();
        });
        mapController.addInsertPostAction(mapCanvas::redraw);
        mapController.addDeletePostAction(mapCanvas::redraw);
        rollbackController.addPostAction(mapCanvas::redraw);
        rollbackController.addPostAction(() -> mapCanvas.setSize(mapView.getMapViewSize()));
    }

    private void addMapObjectsCanvas(Composite mapAndMapObjectsContainer, Shell mainShell) {
        Canvas mapObjectsCanvas = new Canvas(mapAndMapObjectsContainer, SWT.BORDER);
        GridData layoutData = new GridData(SWT.END, SWT.FILL, false, true);
        layoutData.widthHint = MapObjectsView.NUMBER_OF_COLUMNS.value() * MapObjectsView.OBJECT_TILE_SIZE + 1;
        layoutData.heightHint = 700;
        mapObjectsCanvas.setLayoutData(layoutData);
        mapObjectsCanvas.addPaintListener(event -> mapObjectsView.draw(event.gc, mainShell.getDisplay()));
        mapObjectsCanvas.addMouseListener(mapObjectsController);
        mapObjectsController.addPostAction(mapObjectsCanvas::redraw);
        mapController.addLoadMapPostAction(selectedFile -> mapObjectsCanvas.redraw());
        rollbackController.addPostAction(mapObjectsCanvas::redraw);
    }
}
