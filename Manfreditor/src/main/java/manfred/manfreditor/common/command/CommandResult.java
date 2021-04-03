package manfred.manfreditor.common.command;

import io.vavr.control.Try;
import lombok.ToString;

import java.util.function.Consumer;

public abstract class CommandResult {

    public abstract CommandResult registerRollbackOperation(CommandHistory commandHistory);

    public abstract void onFailure(Consumer<String> errorConsumer);

    public abstract Try<RollbackOperation> toTry();

    public static CommandResult successWithRollback(RollbackOperation rollbackOperation) {
        return new Success(rollbackOperation);
    }

    public static CommandResult failure(String errorMessage) {
        return new Failure(errorMessage);
    }

    static class Success extends CommandResult {

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

        @Override
        public Try<RollbackOperation> toTry() {
            return Try.success(rollbackOperation);
        }
    }

    @ToString
    static class Failure extends CommandResult {

        private final String errorMessage;

        private Failure(String errorMessage) {
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

        @Override
        public Try<RollbackOperation> toTry() {
            return Try.failure(new Exception(this.errorMessage));
        }
    }
}
