package manfred.game.controls;

import manfred.game.exception.InvalidInputException;
import manfred.game.graphics.GamePanel;
import manfred.game.interact.gelaber.Gelaber;
import manfred.game.map.MapWrapper;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.function.Consumer;

public class KeyControls implements KeyListener {
    private ManfredController manfredController;
    private GelaberController gelaberController;
    private GamePanel gamePanel;
    private MapWrapper mapWrapper;

    private ControllerInterface activeController;

    public KeyControls(
            ManfredController manfredController,
            GelaberController gelaberController,
            GamePanel gamePanel,
            MapWrapper mapWrapper
    ) {
        this.manfredController = manfredController;
        this.gelaberController = gelaberController;
        this.gamePanel = gamePanel;
        this.mapWrapper = mapWrapper;

        activeController = manfredController;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        activeController.keyPressed(event);
    }

    @Override
    public void keyReleased(KeyEvent event) {
        Consumer<KeyControls> callback = activeController.keyReleased(event);
        if (callback != null) {
            callback.accept(this);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public void controlManfred() {
        activeController = manfredController;
    }

    public void controlGelaber(Gelaber gelaber) {
        activeController = gelaberController;
        gelaberController.setGelaber(gelaber);
    }

    public void loadMap(String name) {
        try {
            mapWrapper.loadMap(name);
        } catch (InvalidInputException | IOException e) {
            System.out.println("Failed to load map " + name);
            e.printStackTrace();
        }
    }
}
