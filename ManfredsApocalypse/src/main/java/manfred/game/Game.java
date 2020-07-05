package manfred.game;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Game {
    public static final String PATH_DATA = "ManfredsApocalypse\\data\\";

    private Thread graphicsPainterThread;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(GameContext.class);
        Game game = context.getBean(Game.class);
        game.run();
    }

    public Game(Thread graphicsPainterThread) {
        this.graphicsPainterThread = graphicsPainterThread;
    }

    public void run() {
        graphicsPainterThread.start();
    }
}
