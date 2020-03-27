package game;

import game.graphics.Graphics;

public class Game {
    private static Graphics graphics;

    public static void main(String[] args) {
        graphics = new Graphics();
        graphics.init();
        graphics.paint();
    }
}
