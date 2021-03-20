package manfred.manfreditor.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.springframework.stereotype.Component;

@Component
public class PopupProvider {

    public int showConfirmationDialog(Shell shell, String message) {
        var confirmationDialog = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
        confirmationDialog.setText("Obacht!");
        confirmationDialog.setMessage(message);
        return confirmationDialog.open();
    }

    public int showMessage(Shell shell, String message) {
        var messageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
        messageBox.setText("Obacht!");
        messageBox.setMessage(message);
        return messageBox.open();
    }
}
