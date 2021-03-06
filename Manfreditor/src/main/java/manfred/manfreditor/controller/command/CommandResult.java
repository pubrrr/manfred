package manfred.manfreditor.controller.command;

import manfred.manfreditor.controller.Failure;
import manfred.manfreditor.controller.Success;

import java.util.function.Consumer;

public interface CommandResult {

    CommandResult registerRollbackOperation(CommandHistory commandHistory);

    void onFailure(Consumer<String> errorConsumer);

    // TODO remove this
    static CommandResult success() {
        return new Success(() -> {});
    }

    static CommandResult success(RollbackOperation rollbackOperation) {
        return new Success(rollbackOperation);
    }

    static CommandResult failure(String errorMessage) {
        return new Failure(errorMessage);
    }
}
