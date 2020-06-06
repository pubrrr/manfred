package manfred.game.characters;

import manfred.game.controls.KeyControls;
import manfred.game.graphics.GamePanel;
import manfred.game.graphics.Paintable;
import manfred.game.interact.Interactable;
import manfred.game.map.MapWrapper;
import org.springframework.lang.Nullable;

import java.awt.*;
import java.util.function.Consumer;

public class Manfred extends MovingObject implements Paintable {
    private static final int INTERACT_DISTANCE = 10;
    private MapWrapper mapWrapper;

    public Manfred(int speed, int x, int y, int healthPoints, MapCollider collider, MapWrapper mapWrapper) {
        super(speed, x, y, GamePanel.PIXEL_BLOCK_SIZE, GamePanel.PIXEL_BLOCK_SIZE, healthPoints, collider);
        this.mapWrapper = mapWrapper;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    @Nullable
    public Consumer<KeyControls> move() {
        super.move();

        Point mainTile = checkForTileManfredStandMainlyOn();
        return mapWrapper.getMap().stepOn(mainTile.x, mainTile.y);
    }

    private Point checkForTileManfredStandMainlyOn() {
        int centerX = x + sizeX / 2;
        int centerY = y + sizeY / 2;

        return new Point(centerX / GamePanel.PIXEL_BLOCK_SIZE, centerY / GamePanel.PIXEL_BLOCK_SIZE);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(getX(), getY(), sizeX, sizeY);

        g.setColor(Color.BLACK);
        switch (viewDirection) {
            case up:
                g.fillRect(x + sizeX / 2, y, 10, 10);
                break;
            case down:
                g.fillRect(x + sizeX / 2, y + sizeY, 10, 10);
                break;
            case left:
                g.fillRect(x, y + sizeY / 2, 10, 10);
                break;
            case right:
                g.fillRect(x + sizeX, y + sizeY / 2, 10, 10);
                break;
        }
    }

    @Nullable
    public Consumer<KeyControls> interact() {
        int triggerInteractPositionX = 0;
        int triggerInteractPositionY = 0;

        switch (viewDirection) {
            case up:
                triggerInteractPositionX = x + sizeX / 2;
                triggerInteractPositionY = y - INTERACT_DISTANCE;
                break;
            case down:
                triggerInteractPositionX = x + sizeX / 2;
                triggerInteractPositionY = y + sizeY + INTERACT_DISTANCE;
                break;
            case left:
                triggerInteractPositionX = x - INTERACT_DISTANCE;
                triggerInteractPositionY = y + sizeY / 2;
                break;
            case right:
                triggerInteractPositionX = x + sizeX + INTERACT_DISTANCE;
                triggerInteractPositionY = y + sizeY / 2;
                break;
        }
        int onMapGridX = triggerInteractPositionX / GamePanel.PIXEL_BLOCK_SIZE;
        int onMapGridY = triggerInteractPositionY / GamePanel.PIXEL_BLOCK_SIZE;

        Interactable interactable = mapWrapper.getMap().getInteractable(onMapGridX, onMapGridY);
        if (interactable == null) {
            return null;
        }
        return interactable.interact();
    }
}
