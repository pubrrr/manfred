package game;

import game.graphics.GraphicsManager;

public class GameRunner implements Runnable {
    final static int REPAINT_PERIOD = 30;

    private GraphicsManager graphicsManager;
    private Manfred manfred;

    public GameRunner(GraphicsManager graphicsManager, Manfred manfred) {
        this.graphicsManager = graphicsManager;
        this.manfred = manfred;
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            move();
            graphicsManager.paint();
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