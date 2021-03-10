package manfred.manfreditor.controller;

import lombok.AllArgsConstructor;
import manfred.manfreditor.controller.command.CommandHistory;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class RollbackController implements SelectionListener {

    private final CommandHistory commandHistory;
    private final List<Runnable> postActions;

    @Override
    public void widgetSelected(SelectionEvent selectionEvent) {
        commandHistory.undoLast();
        postActions.forEach(Runnable::run);
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent selectionEvent) {
    }

    public void addPostAction(Runnable postAction) {
        this.postActions.add(postAction);
    }
}
