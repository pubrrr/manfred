package manfred.manfreditor.controller;

import lombok.ToString;
import manfred.manfreditor.controller.command.CommandHistory;
import manfred.manfreditor.controller.command.CommandResult;

import java.util.function.Consumer;

@ToString
public class Failure implements CommandResult {

    private final String errorMessage;

    public Failure(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public CommandResult registerRollbackOperation(CommandHistory commandHistory) {
        return this;
    }

    @Override
    public void onFailure(Consumer<String> errorConsumer) {
        errorConsumer.accept(errorMessage);
    }
}
