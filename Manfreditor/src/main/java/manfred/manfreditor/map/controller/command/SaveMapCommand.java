package manfred.manfreditor.map.controller.command;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import manfred.data.persistence.PreviousFileContent;
import manfred.manfreditor.common.FileHelper;
import manfred.manfreditor.common.PopupProvider;
import manfred.manfreditor.common.command.Command;
import manfred.manfreditor.common.command.CommandResult;
import manfred.manfreditor.common.command.RollbackOperation;
import manfred.manfreditor.map.model.MapModel;
import manfred.manfreditor.map.model.export.MapExporter;
import manfred.manfreditor.map.model.flattened.FlattenedMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@AllArgsConstructor
public class SaveMapCommand implements Command {

    private final FlattenedMap flattenedMap;
    private final MapExporter mapExporter;
    private final FileHelper fileHelper;
    private final PopupProvider popupProvider;
    private final Shell outputShell;

    @Override
    public CommandResult execute() {
        return mapExporter.export(this.flattenedMap)
            .fold(
                exception -> CommandResult.failure(exception.getMessage()),
                successWithFileRestore()
            );
    }

    private Function<Option<PreviousFileContent>, CommandResult> successWithFileRestore() {
        return optionalPreviousContent -> optionalPreviousContent.fold(
            () -> CommandResult.successWithRollback(deleteCreatedFile()),
            previousFileContent -> CommandResult.successWithRollback(tryToRestorePreviousContent(previousFileContent))
        );
    }

    private RollbackOperation deleteCreatedFile() {
        return () -> {
            int clickedButton = this.popupProvider.showConfirmationDialog(outputShell, "Wirklich Map " + flattenedMap.getName() + " wieder löschen?");
            if (clickedButton == SWT.YES) {
                flattenedMap.getMapSource().getMapFile().delete();
            }
        };
    }

    private RollbackOperation tryToRestorePreviousContent(PreviousFileContent previousFileContent) {
        return () -> {
            int clickedButton = this.popupProvider.showConfirmationDialog(outputShell, "Wirklich Speichern von " + flattenedMap.getName() + " rückgängig machen?");
            if (clickedButton == SWT.YES) {
                fileHelper.write(flattenedMap.getMapSource().getMapFile(), previousFileContent.getContent())
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

        public Command create(Shell outputShell) {
            return new SaveMapCommand(mapModel.getFlattenedMap(), mapExporter, fileHelper, popupProvider, outputShell);
        }
    }
}
