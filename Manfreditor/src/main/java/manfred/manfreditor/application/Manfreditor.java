package manfred.manfreditor.application;

import manfred.manfreditor.common.command.Command;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Manfreditor {

    private final GuiBuilder guiBuilder;
    private final List<Command> startupCommands;

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ManfreditorContext.class)) {
            context.getBean(Manfreditor.class).run();
        }
    }

    public Manfreditor(GuiBuilder guiBuilder, @Qualifier("StartupCommands")List<Command> startupCommands) {
        this.guiBuilder = guiBuilder;
        this.startupCommands = startupCommands;
    }

    public void run() {
        startupCommands.forEach(Command::execute);
        guiBuilder.build().show();
    }
}