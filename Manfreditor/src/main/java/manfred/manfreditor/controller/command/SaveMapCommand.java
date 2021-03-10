package manfred.manfreditor.controller.command;

import org.springframework.stereotype.Component;

public class SaveMapCommand implements Command {

    @Override
    public CommandResult execute() {
        return CommandResult.failure("wois au id so genau warom jez");
    }

    @Component
    public static class Factory {

        public Command create() {
            return new SaveMapCommand();
        }
    }
}
