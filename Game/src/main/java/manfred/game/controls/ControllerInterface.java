package manfred.game.controls;

import javax.swing.*;
import java.awt.event.KeyEvent;

public interface ControllerInterface {
    ControllerInterface keyPressed(KeyEvent event);

    ControllerInterface keyReleased(KeyEvent event);

    void stop();

    ControllerInterface move();

    static ControllerInterface self(ControllerInterface self) {
        return self;
    }

    static ControllerInterface sleepWhileWorkingOn(SwingWorker<ManfredController, Void> worker) {
        SleepingController sleepingController = new SleepingController(worker);
        worker.execute();
        return sleepingController;
    }
}