package manfred.game.controls;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class SleepingController implements ControllerInterface {
    private final SwingWorker<ManfredController, Void> worker;

    public SleepingController(SwingWorker<ManfredController, Void> worker) {
        this.worker = worker;
    }

    @Override
    public ControllerInterface keyPressed(KeyEvent event) {
        Optional<ControllerInterface> controllerInterface = checkWorkerStatus();
        return controllerInterface.map(controller -> controller.keyPressed(event)).orElse(this);
    }

    @Override
    public ControllerInterface keyReleased(KeyEvent event) {
        Optional<ControllerInterface> controllerInterface = checkWorkerStatus();
        return controllerInterface.map(controller -> controller.keyReleased(event)).orElse(this);
    }

    private Optional<ControllerInterface> checkWorkerStatus() {
        if (!worker.isDone()) {
            return Optional.empty();
        }

        try {
            return Optional.of(worker.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {
        // do nothing
    }

    @Override
    public ControllerInterface move() {
        return this;
    }
}
