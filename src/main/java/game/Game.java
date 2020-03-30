package game;

public class Game {
    private Thread graphicsPainterThread;

    public static void main(String[] args) {
        GameFactory factory = new GameFactory();
        factory.create().run();
    }

    public Game(Thread graphicsPainterThread) {
        this.graphicsPainterThread = graphicsPainterThread;
    }

    public void run() {
        graphicsPainterThread.start();
    }
}
