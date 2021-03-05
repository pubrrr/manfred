package manfred.manfreditor.controller;

import manfred.manfreditor.controller.command.Command;
import manfred.manfreditor.controller.command.CommandResult;

public class ControllerHelper {

    public static final int LEFT_MOUSE_BUTTON = 1;
    public static final int RIGHT_MOUSE_BUTTON = 3;

    public static CommandResult execute(Command command) {
        try {
            return command.execute();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return CommandResult.failure("Da isch ebbes schief glaufa: " + throwable.toString());
        }
    }
}
