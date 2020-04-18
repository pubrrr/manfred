package manfred.game.controls;

import manfred.game.graphics.GamePanel;
import manfred.game.interact.gelaber.Gelaber;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyControls implements KeyListener {
    private ManfredController manfredController;
    private GelaberController gelaberController;
    private GamePanel gamePanel;

    private ControllerInterface activeController;

    public KeyControls(ManfredController manfredController, GelaberController gelaberController, GamePanel panel) {
        this.manfredController = manfredController;
        this.gelaberController = gelaberController;
        this.gamePanel = panel;

        activeController = manfredController;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        activeController.keyPressed(event);
    }

    @Override
    public void keyReleased(KeyEvent event) {
        activeController.keyReleased(event);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    void controlManfred() {
        activeController = manfredController;
    }

    void controlGelaber(Gelaber gelaber) {
        activeController = gelaberController;

        gamePanel.registerPaintable(gelaber);
        gelaberController.setGelaber(gelaber);
    }
}
