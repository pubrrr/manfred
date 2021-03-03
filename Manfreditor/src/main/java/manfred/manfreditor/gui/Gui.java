package manfred.manfreditor.gui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Gui {

    private final Shell mainShell;
    private final Display mainDisplay;

    public Gui(Shell mainShell, Display mainDisplay) {
        this.mainShell = mainShell;
        this.mainDisplay = mainDisplay;
    }

    public void show() {
        mainShell.open();
        while (!mainShell.isDisposed()) {
            if (!mainDisplay.readAndDispatch()) {
                mainDisplay.sleep();
            }
        }
        mainDisplay.dispose();
    }
}
