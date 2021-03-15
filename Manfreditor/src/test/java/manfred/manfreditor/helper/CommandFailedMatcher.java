package manfred.manfreditor.helper;

import manfred.manfreditor.controller.command.CommandResult;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class CommandFailedMatcher extends BaseMatcher<CommandResult> {

    private final String expectedMessage;

    private CommandFailedMatcher(String expectedMessage) {
        this.expectedMessage = expectedMessage;
    }

    public static CommandFailedMatcher failedWithMessageContaining(String expectedMessage) {
        return new CommandFailedMatcher(expectedMessage);
    }

    @Override
    public boolean matches(Object actual) {
        if (!(actual instanceof CommandResult)) {
            return false;
        }
        StringBuilder actualErrorMessage = new StringBuilder();
        ((CommandResult) actual).onFailure(actualErrorMessage::append);
        return actualErrorMessage.toString().contains(expectedMessage);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Failure with message '" + expectedMessage + "'");
    }
}
