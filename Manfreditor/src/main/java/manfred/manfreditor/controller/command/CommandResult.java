package manfred.manfreditor.controller.command;

import manfred.manfreditor.controller.Failure;
import manfred.manfreditor.controller.Success;

import java.util.function.Consumer;

public interface CommandResult {

    void onFailure(Consumer<String> errorConsumer);

    static CommandResult success() {
        return new Success();
    }

    static CommandResult failure(String errorMessage) {
        return new Failure(errorMessage);
    }
}
