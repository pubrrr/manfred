package manfred.manfreditor.controller;

import lombok.AllArgsConstructor;
import manfred.manfreditor.controller.command.CommandResult;
import manfred.manfreditor.controller.command.LoadMapCommand;
import manfred.manfreditor.controller.command.LoadMapObjectCommand;
import org.springframework.stereotype.Component;

import static manfred.manfreditor.controller.ControllerHelper.execute;

@Component
@AllArgsConstructor
public class MapController {

    private final LoadMapCommand.Factory loadMapCommandFactory;

    public CommandResult loadMap(String selectedFile) {
        return execute(loadMapCommandFactory.create(selectedFile));
    }
}
