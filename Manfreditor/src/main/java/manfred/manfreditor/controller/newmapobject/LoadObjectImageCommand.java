package manfred.manfreditor.controller.newmapobject;

import lombok.AllArgsConstructor;
import manfred.manfreditor.controller.command.Command;
import manfred.manfreditor.controller.command.CommandResult;
import manfred.manfreditor.mapobject.NewMapObjectModel;
import org.eclipse.swt.graphics.ImageLoader;
import org.springframework.stereotype.Component;

import static io.vavr.API.TODO;

@AllArgsConstructor
public class LoadObjectImageCommand implements Command {

    private final ImageLoader imageLoader;
    private final NewMapObjectModel newMapObjectModel;
    private final String imagePath;

    @Override
    public CommandResult execute() {
        return TODO();
    }

    @Component
    @AllArgsConstructor
    public static class Factory {

        private final ImageLoader imageLoader;
        private final NewMapObjectModel newMapObjectModel;

        public Command create(String imagePath) {
            return new LoadObjectImageCommand(imageLoader, newMapObjectModel, imagePath);
        }
    }
}
