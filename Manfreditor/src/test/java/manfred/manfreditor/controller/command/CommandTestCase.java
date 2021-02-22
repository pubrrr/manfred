package manfred.manfreditor.controller.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public abstract class CommandTestCase {

    public static void assertCommandFailed(CommandResult result, String expectedErrorMessage) {
        StringBuilder actualErrorMessage = new StringBuilder();
        result.onFailure(actualErrorMessage::append);
        assertThat(actualErrorMessage.toString(), is(expectedErrorMessage));
    }
}
