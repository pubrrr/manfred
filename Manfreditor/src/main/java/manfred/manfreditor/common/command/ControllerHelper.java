package manfred.manfreditor.common.command;

import lombok.AllArgsConstructor;
import manfred.manfreditor.common.command.Command;
import manfred.manfreditor.common.command.CommandHistory;
import manfred.manfreditor.common.command.CommandResult;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ControllerHelper {

    public static final int LEFT_MOUSE_BUTTON = 1;
    public static final int RIGHT_MOUSE_BUTTON = 3;
    public static final int KEY_ENTER = 13;

    private final CommandHistory commandHistory;

    public CommandResult execute(Command command) {
        try {
            return command.execute().registerRollbackOperation(commandHistory);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return CommandResult.failure("Da isch ebbes schief glaufa: " + throwable.toString());
        }
    }
}
