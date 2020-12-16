package manfred.game.controls;

import manfred.game.graphics.GamePanel;
import manfred.game.interact.gelaber.Gelaber;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyControls implements KeyListener {
    private final GamePanel gamePanel;

    private ControllerInterface activeController;

    public KeyControls(ControllerInterface activeController, GamePanel gamePanel) {
        this.activeController = activeController;
        this.gamePanel = gamePanel;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    @Override
    public synchronized void keyPressed(KeyEvent event) {
        activeController = activeController.keyPressed(event);
    }

    @Override
    public synchronized void keyReleased(KeyEvent event) {
        activeController = activeController.keyReleased(event);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public void controlManfred() {
//        activeController = manfredController;
    }

    public void controlGelaber(Gelaber gelaber) {
        this.turnOffControls();
//        activeController = gelaberController;
//        gelaberController.setGelaber(gelaber);
        this.getGamePanel().registerGelaberToPaint(gelaber);
    }

    public void turnOffControls() {
        activeController.stop();
//        activeController = doNothingController;
    }

    public void doNothing() {
        // do nothing
    }

    public synchronized void move() {
        activeController = activeController.move();
    }
}
