package manfred.manfreditor.controller;

import lombok.AllArgsConstructor;
import manfred.manfreditor.controller.command.Command;
import manfred.manfreditor.controller.command.CommandResult;
import manfred.manfreditor.controller.command.LoadMapCommand;
import manfred.manfreditor.controller.command.LoadMapObjectCommand;
import manfred.manfreditor.controller.command.SelectMapObjectCommand;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GuiController {

    private final LoadMapCommand.Factory loadMapCommandFactory;
    private final LoadMapObjectCommand.Factory loadMapObjectCommandFactory;
    private final SelectMapObjectCommand.Factory selectMapObjectCommandFactory;


    public CommandResult loadMap(String selectedFile) {
        return execute(loadMapCommandFactory.create(selectedFile));
    }

    private CommandResult execute(Command command) {
        try {
            return command.execute();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return CommandResult.failure(throwable.toString());
        }
    }
}
