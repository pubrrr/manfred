package manfred.manfreditor.controller;

import manfred.manfreditor.controller.command.CommandResult;

import java.util.function.Consumer;

public class Failure implements CommandResult {

    private final String errorMessage;

    public Failure(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public void onFailure(Consumer<String> errorConsumer) {
        errorConsumer.accept(errorMessage);
    }
}