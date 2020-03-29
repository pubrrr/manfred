package game;

import game.graphics.GraphicsManager;

public class Game {
    private GraphicsManager graphics;

    public static void main(String[] args) {
        GameFactory factory = new GameFactory();
        factory.create().run();
    }

    public Game(GraphicsManager graphics) {
        this.graphics = graphics;
    }

    public void run() {
        graphics.paint();
    }
}
