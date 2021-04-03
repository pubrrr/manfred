package manfred.manfreditor.newmapobject.controller.command;

import lombok.AllArgsConstructor;
import manfred.manfreditor.controller.command.Command;
import manfred.manfreditor.controller.command.CommandResult;
import manfred.manfreditor.newmapobject.model.NewMapObjectModel;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static io.vavr.API.Try;
import static manfred.manfreditor.controller.command.CommandResult.failure;
import static manfred.manfreditor.controller.command.CommandResult.successWithRollback;

@AllArgsConstructor
public class LoadObjectImageCommand implements Command {

    private final ImageLoader imageLoader;
    private final NewMapObjectModel newMapObjectModel;
    private final String imagePath;

    @Override
    public CommandResult execute() {
        Optional<ImageData> previousImageData = newMapObjectModel.getImageData();

        return Try(() -> imageLoader.load(this.imagePath))
            .map(imageDataArray -> imageDataArray[0])
            .peek(newMapObjectModel::setImageData)
            .fold(
                throwable -> failure(throwable.getMessage()),
                imageData -> successWithRollback(() -> newMapObjectModel.setImageData(previousImageData.orElse(null)))
            );
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
