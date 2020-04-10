package manfred.game;

import manfred.game.characters.Manfred;
import manfred.game.graphics.ManfredWindow;

public class GameRunner implements Runnable {
    final static int REPAINT_PERIOD = 30;

    private ManfredWindow window;
    private Manfred manfred;

    public GameRunner(ManfredWindow window, Manfred manfred) {
        this.window = window;
        this.manfred = manfred;
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            move();
            window.repaint();
            try {
                Thread.sleep(REPAINT_PERIOD);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void move() {
        manfred.move();
    }
}