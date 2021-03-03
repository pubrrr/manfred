package manfred.manfreditor;

import manfred.manfreditor.gui.GuiBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Manfreditor {

    private final GuiBuilder guiBuilder;

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ManfreditorContext.class)) {
            context.getBean(Manfreditor.class).run();
        }
    }

    public Manfreditor(GuiBuilder guiBuilder) {
        this.guiBuilder = guiBuilder;
    }

    public void run() {
        guiBuilder.build().show();
    }
}