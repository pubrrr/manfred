package manfred.manfreditor.helper;

import manfred.manfreditor.common.command.CommandResult;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.function.Consumer;

public class SuccessfulCommandMatcher extends BaseMatcher<CommandResult> {

    public static SuccessfulCommandMatcher wasSuccessful() {
        return new SuccessfulCommandMatcher();
    }

    @Override
    public boolean matches(Object actual) {
        if (!(actual instanceof CommandResult)) {
            return false;
        }

        CommandResultCaptor commandResultCaptor = captureFailure((CommandResult) actual);
        return !commandResultCaptor.wasCalled();
    }

    private CommandResultCaptor captureFailure(CommandResult actual) {
        CommandResultCaptor commandResultCaptor = new CommandResultCaptor();
        actual.onFailure(commandResultCaptor);
        return commandResultCaptor;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("successful command result");
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        if (!(item instanceof CommandResult)) {
            super.describeMismatch(item, description);
            return;
        }

        CommandResultCaptor commandResultCaptor = captureFailure((CommandResult) item);
        description.appendText("command failed with message: " + commandResultCaptor.getCapturedMessage());
    }

    private static class CommandResultCaptor implements Consumer<String> {

        private String capturedMessage = "";
        private boolean wasCalled = false;

        @Override
        public void accept(String message) {
            this.capturedMessage = message;
            wasCalled = true;
        }

        public String getCapturedMessage() {
            return capturedMessage;
        }

        public boolean wasCalled() {
            return wasCalled;
        }
    }
}
