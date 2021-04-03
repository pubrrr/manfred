package manfred.manfreditor.common.command;

import org.springframework.stereotype.Component;

import java.util.EmptyStackException;
import java.util.Stack;

@Component
public class CommandHistory {

    private final Stack<RollbackOperation> history = new Stack<>();

    public void push(RollbackOperation rollbackOperation) {
        history.push(rollbackOperation);
    }

    public void undoLast() {
        try {
            history.pop().rollback();
        } catch (EmptyStackException e) {
            // mir doch wurscht
        }
    }
}
