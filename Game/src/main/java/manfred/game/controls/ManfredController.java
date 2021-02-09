package manfred.game.controls;

import lombok.AllArgsConstructor;
import manfred.data.InvalidInputException;
import manfred.data.shared.PositiveInt;
import manfred.game.attack.AttacksContainer;
import manfred.game.attack.Caster;
import manfred.game.attack.CombinationElement;
import manfred.game.characters.Manfred;
import manfred.game.graphics.scrolling.BackgroundScroller;
import manfred.game.graphics.GamePanel;
import manfred.game.graphics.PanelCoordinate;
import manfred.game.graphics.coordinatetransformation.MapCoordinateToPanelCoordinateTransformer;
import manfred.game.graphics.paintable.GelaberOverlay;
import manfred.game.interact.person.gelaber.GelaberFacade;
import manfred.game.map.ManfredPositionSetter;
import manfred.game.map.Map;
import manfred.game.map.MapFacade;
import org.springframework.stereotype.Component;

import java.awt.event.KeyEvent;

@Component
@AllArgsConstructor
public class ManfredController implements ControllerInterface {
    private final Manfred manfred;
    private final Caster attackCaster;
    private final MapFacade mapFacade;
    private final BackgroundScroller backgroundScroller;
    private final GamePanel gamePanel;
    private final AttacksContainer attacksContainer;
    private final GelaberOverlay gelaberOverlay;
    private final ObjectsMover objectsMover;
    private final MapCoordinateToPanelCoordinateTransformer mapCoordinateToPanelCoordinateTransformer;

    @Override
    public ControllerInterface keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_A:
                manfred.left();
                break;
            case KeyEvent.VK_D:
                manfred.right();
                break;
            case KeyEvent.VK_S:
                manfred.down();
                break;
            case KeyEvent.VK_W:
                manfred.up();
                break;
            case KeyEvent.VK_SPACE:
                attackCaster.cast(manfred.getCenter(), manfred.getDirection());
                break;
            default:
                CombinationElement.fromKeyEvent(event).ifPresent(attackCaster::addToCombination);
                break;
        }
        return this;
    }

    @Override
    public ControllerInterface keyReleased(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_A:
            case KeyEvent.VK_D:
                manfred.stopX();
                manfred.checkForVerticalViewDirection();
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_W:
                manfred.stopY();
                manfred.checkForHorizontalViewDirection();
                break;
            case KeyEvent.VK_ENTER:
                Map.TileCoordinate interactionMapTile = manfred.getInteractionMapTile();
                return mapFacade.interactWithTile(interactionMapTile).determineNewControllerState(this);
        }
        return this;
    }

    @Override
    public void stop() {
        manfred.stopX();
        manfred.stopY();
        attackCaster.off();
    }

    @Override
    public ControllerInterface move() {
        return this.objectsMover.move().determineNewControllerState(this);
    }

    public void loadMap(String name, PositiveInt targetSpawnX, PositiveInt targetSpawnY) {
        try {
            attacksContainer.clear();
            mapFacade.loadMap(name, new ManfredPositionSetter(this.manfred, targetSpawnX, targetSpawnY));

            PanelCoordinate newManfredCenterCoordinate = mapCoordinateToPanelCoordinateTransformer.toPanelCoordinate(this.manfred.getCenter());
            this.backgroundScroller.centerTo(newManfredCenterCoordinate);
        } catch (InvalidInputException e) {
            System.out.println("ERROR: Failed to load map " + name + "\n");
            e.printStackTrace();
        }
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public ControllerInterface talk(ManfredController self, GelaberFacade gelaberFacade) {
        self.stop();
        gelaberOverlay.setGelaber(gelaberFacade);
        return new GelaberController(self, gelaberFacade, gelaberOverlay);
    }
}
