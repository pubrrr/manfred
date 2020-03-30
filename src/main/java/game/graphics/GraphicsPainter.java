package game.graphics;

public class GraphicsPainter implements Runnable {
    final static int REPAINT_PERIOD = 30;

    private GraphicsManager graphicsManager;

    public GraphicsPainter(GraphicsManager graphicsManager) {
        this.graphicsManager = graphicsManager;
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            graphicsManager.paint();
            try {
                Thread.sleep(REPAINT_PERIOD);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}