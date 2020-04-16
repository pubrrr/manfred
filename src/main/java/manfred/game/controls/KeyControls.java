package manfred.game.controls;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyControls implements KeyListener {
    private ManfredController manfredController;
    private GelaberController gelaberController;

    private ControllerInterface activeController;

    public KeyControls(ManfredController manfredController, GelaberController gelaberController) {
        this.manfredController = manfredController;
        this.gelaberController = gelaberController;

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

    void controlGelaber() {
        activeController = gelaberController;
    }
}
