package manfred.manfreditor.controller;

import manfred.manfreditor.controller.command.CommandHistory;
import manfred.manfreditor.controller.command.CommandResult;
import manfred.manfreditor.controller.command.RollbackOperation;

import java.util.function.Consumer;

public class Success implements CommandResult {

    private final RollbackOperation rollbackOperation;

    public Success(RollbackOperation rollbackOperation) {
        this.rollbackOperation = rollbackOperation;
    }

    @Override
    public CommandResult registerRollbackOperation(CommandHistory commandHistory) {
        commandHistory.push(rollbackOperation);
        return this;
    }

    @Override
    public void onFailure(Consumer<String> errorConsumer) {
        // do nothing
    }
}
