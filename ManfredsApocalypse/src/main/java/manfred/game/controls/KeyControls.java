package manfred.game.controls;

import manfred.game.GameConfig;
import manfred.game.characters.Manfred;
import manfred.game.exception.InvalidInputException;
import manfred.game.graphics.BackgroundScroller;
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
    private DoNothingController doNothingController;
    private Manfred manfred;
    private GamePanel gamePanel;
    private MapWrapper mapWrapper;
    private GameConfig gameConfig;
    private BackgroundScroller backgroundScroller;

    private ControllerInterface activeController;

    public KeyControls(
            ManfredController manfredController,
            GelaberController gelaberController,
            DoNothingController doNothingController,
            Manfred manfred,
            GamePanel gamePanel,
            MapWrapper mapWrapper,
            GameConfig gameConfig,
            BackgroundScroller backgroundScroller
    ) {
        this.manfredController = manfredController;
        this.gelaberController = gelaberController;
        this.doNothingController = doNothingController;
        this.manfred = manfred;
        this.gamePanel = gamePanel;
        this.mapWrapper = mapWrapper;
        this.gameConfig = gameConfig;
        this.backgroundScroller = backgroundScroller;

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

    public void turnOffControls() {
        manfred.stopX();
        manfred.stopY();
        activeController = doNothingController;
    }

    public void loadMap(String name) {
        try {
            mapWrapper.loadMap(name);
        } catch (InvalidInputException | IOException e) {
            System.out.println("ERROR: Failed to load map " + name + "\n");
            e.printStackTrace();
        }
    }

    public void resetManfredPositionTo(int x, int y) {
        this.manfred.setX(gameConfig.getPixelBlockSize() * x);
        this.manfred.setY(gameConfig.getPixelBlockSize() * y);
        backgroundScroller.centerTo(manfred.getSprite().getCenter());
    }
}
