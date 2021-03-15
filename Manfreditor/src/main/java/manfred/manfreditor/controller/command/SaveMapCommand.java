package manfred.manfreditor.controller.command;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import manfred.data.persistence.PreviousFileContent;
import manfred.manfreditor.common.FileWriter;
import manfred.manfreditor.map.MapModel;
import manfred.manfreditor.map.export.MapExporter;
import manfred.manfreditor.map.flattened.FlattenedMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.function.Function;

@AllArgsConstructor
public class SaveMapCommand implements Command {

    private final MapModel mapModel;
    private final MapExporter mapExporter;
    private final FileWriter fileWriter;
    private final File fileToSaveIn;
    private final Shell outputShell;

    @Override
    public CommandResult execute() {
        FlattenedMap flattenedMap = mapModel.getFlattenedMap();
        return mapExporter.export(flattenedMap, fileToSaveIn)
            .fold(
                exception -> CommandResult.failure(exception.getMessage()),
                successWithFileRestore()
            );
    }

    private Function<Option<PreviousFileContent>, CommandResult> successWithFileRestore() {
        return optionalPreviousContent -> optionalPreviousContent.fold(
            () -> CommandResult.success(fileToSaveIn::delete),
            previousFileContent -> CommandResult.success(tryToRestorePreviousContent(previousFileContent, outputShell))
        );
    }

    private RollbackOperation tryToRestorePreviousContent(PreviousFileContent previousFileContent, Shell outputShell) {
        return () -> fileWriter.write(fileToSaveIn, previousFileContent.getContent())
            .onFailure(throwable -> {
                var messageBox = new MessageBox(outputShell, SWT.ICON_ERROR | SWT.OK);
                messageBox.setMessage("Restoring previous file content failed:\n" + throwable.getMessage());
            });
    }

    @Component
    @AllArgsConstructor
    public static class Factory {

        private final MapModel mapModel;
        private final MapExporter mapExporter;
        private final FileWriter fileWriter;

        public Command create(File fileToSaveIn, Shell outputShell) {
            return new SaveMapCommand(mapModel, mapExporter, fileWriter, fileToSaveIn, outputShell);
        }
    }
}
