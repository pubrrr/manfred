package manfred.game;

import manfred.game.controls.KeyControls;
import manfred.game.graphics.ManfredWindow;
import org.springframework.stereotype.Component;

@Component
public class GameRunner implements Runnable {
    final static int REPAINT_PERIOD = 15;

    private final KeyControls keyControls;
    private final ManfredWindow window;

    public GameRunner(KeyControls keyControls, ManfredWindow window) {
        this.keyControls = keyControls;
        this.window = window;
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            long beforeTime = System.currentTimeMillis();
            this.keyControls.move();
            window.repaint();

            long timeNeeded = System.currentTimeMillis() - beforeTime;
            try {
                Thread.sleep(
                        REPAINT_PERIOD - timeNeeded > 0
                                ? REPAINT_PERIOD - timeNeeded
                                : 1
                );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}