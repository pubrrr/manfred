package game;

import game.exception.InvalidInputException;

import java.io.IOException;

public class Game {
    private Thread graphicsPainterThread;

    public static void main(String[] args) {
        GameFactory factory = new GameFactory();
        try {
            factory.create().run();
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            String message = "Could not read map: " + e.getMessage();
            System.out.println(message);
            e.printStackTrace();
        }
    }

    public Game(Thread graphicsPainterThread) {
        this.graphicsPainterThread = graphicsPainterThread;
    }

    public void run() {
        graphicsPainterThread.start();
    }
}
