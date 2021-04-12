package manfred.manfreditor.common;

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

    public void showMessage(Shell shell, String message) {
        var messageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
        messageBox.setText("Obacht!");
        messageBox.setMessage(message);
        messageBox.open();
    }

    public void showInformation(Shell shell, String message) {
        var messageBox = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
        messageBox.setText("OK!!!1!");
        messageBox.setMessage(message);
        messageBox.open();
    }
}
