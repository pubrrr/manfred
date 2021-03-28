package manfred.manfreditor.controller.command;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import manfred.data.persistence.PreviousFileContent;
import manfred.manfreditor.common.FileHelper;
import manfred.manfreditor.gui.PopupProvider;
import manfred.manfreditor.map.MapModel;
import manfred.manfreditor.map.export.MapExporter;
import manfred.manfreditor.map.flattened.FlattenedMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.function.Function;

@AllArgsConstructor
public class SaveMapCommand implements Command {

    private final MapModel mapModel;
    private final MapExporter mapExporter;
    private final FileHelper fileHelper;
    private final PopupProvider popupProvider;
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
            () -> CommandResult.success(deleteCreatedFile()),
            previousFileContent -> CommandResult.success(tryToRestorePreviousContent(previousFileContent))
        );
    }

    private RollbackOperation deleteCreatedFile() {
        return () -> {
            int clickedButton = this.popupProvider.showConfirmationDialog(outputShell, "Wirklich " + fileToSaveIn.getName() + " wieder löschen?");
            if (clickedButton == SWT.YES) {
                fileToSaveIn.delete();
            }
        };
    }

    private RollbackOperation tryToRestorePreviousContent(PreviousFileContent previousFileContent) {
        return () -> {
            int clickedButton = this.popupProvider.showConfirmationDialog(outputShell, "Wirklich alten Zustand von " + fileToSaveIn.getName() + " wiederherstellen?");
            if (clickedButton == SWT.YES) {
                fileHelper.write(fileToSaveIn, previousFileContent.getContent())
                    .onFailure(throwable -> popupProvider.showMessage(
                        outputShell,
                        "Restoring previous file content failed:\n" + throwable.getMessage())
                    );
            }
        };
    }

    @Component
    @AllArgsConstructor
    public static class Factory {

        private final MapModel mapModel;
        private final MapExporter mapExporter;
        private final FileHelper fileHelper;
        private final PopupProvider popupProvider;

        public Command create(File fileToSaveIn, Shell outputShell) {
            return new SaveMapCommand(mapModel, mapExporter, fileHelper, popupProvider, fileToSaveIn, outputShell);
        }
    }
}
