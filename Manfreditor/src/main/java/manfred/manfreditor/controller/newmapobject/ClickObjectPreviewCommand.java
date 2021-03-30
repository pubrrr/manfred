package manfred.manfreditor.controller.newmapobject;

import manfred.manfreditor.controller.command.Command;
import manfred.manfreditor.controller.command.CommandResult;
import org.springframework.stereotype.Component;

public class ClickObjectPreviewCommand implements Command {
    @Override
    public CommandResult execute() {
        return null;
    }

    @Component
    public static class Factory {

        public ClickObjectPreviewCommand create(int x, int y) {
            return null;
        }
    }
}
