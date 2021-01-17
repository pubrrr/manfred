package manfred.game;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Game {
    private final Thread graphicsPainterThread;

    public static void main(String[] args) {
    	try(AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(GameContext.class)){
    		Game game = context.getBean(Game.class);
    		game.run();
    	}
    }

    public Game(Thread graphicsPainterThread) {
        this.graphicsPainterThread = graphicsPainterThread;
    }

    public void run() {
        graphicsPainterThread.start();
    }
}
