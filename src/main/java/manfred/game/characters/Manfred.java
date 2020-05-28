package manfred.game.characters;

import manfred.game.controls.KeyControls;
import manfred.game.graphics.GamePanel;
import manfred.game.graphics.Paintable;
import manfred.game.interact.Interactable;
import manfred.game.map.MapWrapper;
import org.springframework.lang.Nullable;

import java.awt.*;
import java.util.function.Consumer;

public class Manfred implements Paintable {
    private static final int SPEED = 10;
    private static final int INTERACT_DISTANCE = 10;

    private int x;
    private int y;

    private int currentSpeedX = 0;
    private int currentSpeedY = 0;
    private boolean movesLeft = false;
    private boolean movesRight = false;
    private boolean movesUp = false;
    private boolean movesDown = false;
    private Direction viewDirection = Direction.down;

    private final int sizeX;
    private final int sizeY;

    private MapCollider collider;
    private MapWrapper mapWrapper;

    public Manfred(int x, int y, MapCollider collider, MapWrapper mapWrapper) {
        this.x = x;
        this.y = y;
        sizeX = GamePanel.PIXEL_BLOCK_SIZE;
        sizeY = GamePanel.PIXEL_BLOCK_SIZE;
        this.collider = collider;
        this.mapWrapper = mapWrapper;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void left() {
        if (!movesLeft) {
            viewDirection = Direction.left;
            movesLeft = true;
            currentSpeedX -= SPEED;
        }
    }

    public void right() {
        if (!movesRight) {
            viewDirection = Direction.right;
            movesRight = true;
            currentSpeedX += SPEED;
        }
    }

    public void up() {
        if (!movesUp) {
            viewDirection = Direction.up;
            movesUp = true;
            // y-Achse ist invertiert: kleiner Werte werden weiter oben gezeichnet
            currentSpeedY -= SPEED;
        }
    }

    public void down() {
        if (!movesDown) {
            viewDirection = Direction.down;
            movesDown = true;
            currentSpeedY += SPEED;
        }
    }

    public void stopX() {
        movesRight = false;
        movesLeft = false;
        currentSpeedX = 0;
    }

    public void stopY() {
        movesUp = false;
        movesDown = false;
        currentSpeedY = 0;
    }

    public void move() {
        if (!collider.collides(
                x + currentSpeedX,
                x + currentSpeedX + (sizeX - 1),
                y,
                y + (sizeY - 1)
        )) {
            x += currentSpeedX;
        }

        if (!collider.collides(
                x,
                x + (sizeX - 1),
                y + currentSpeedY,
                y + currentSpeedY + (sizeY - 1)
        )) {
            y += currentSpeedY;
        }
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(getX(), getY(), sizeX, sizeY);

        g.setColor(Color.BLACK);
        switch (viewDirection) {
            case up:
                g.fillRect(x + sizeX/2, y, 10, 10);
                break;
            case down:
                g.fillRect(x + sizeX/2, y + sizeY, 10, 10);
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
