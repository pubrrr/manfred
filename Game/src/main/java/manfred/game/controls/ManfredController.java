package manfred.game.controls;

import manfred.data.InvalidInputException;
import manfred.game.config.GameConfig;
import manfred.game.attack.Attack;
import manfred.game.attack.AttacksContainer;
import manfred.game.attack.Caster;
import manfred.game.attack.CombinationElement;
import manfred.game.characters.Manfred;
import manfred.game.enemy.EnemiesWrapper;
import manfred.game.exception.ManfredException;
import manfred.game.graphics.BackgroundScroller;
import manfred.game.graphics.GamePanel;
import manfred.game.graphics.paintable.GelaberOverlay;
import manfred.game.interact.person.gelaber.GelaberFacade;
import manfred.game.map.MapFacade;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.event.KeyEvent;

@Component
public class ManfredController implements ControllerInterface {
    private final Manfred manfred;
    private final Caster attackCaster;
    private final MapFacade mapFacade;
    private final GameConfig gameConfig;
    private final BackgroundScroller backgroundScroller;
    private final GamePanel gamePanel;
    private final AttacksContainer attacksContainer;
    private final EnemiesWrapper enemiesWrapper;
    private final GelaberOverlay gelaberOverlay;

    public ManfredController(
        Manfred manfred,
        Caster attackCaster,
        MapFacade mapFacade,
        GameConfig gameConfig,
        BackgroundScroller backgroundScroller,
        GamePanel gamePanel,
        AttacksContainer attacksContainer,
        EnemiesWrapper enemiesWrapper,
        GelaberOverlay gelaberOverlay
    ) {
        this.manfred = manfred;
        this.attackCaster = attackCaster;
        this.mapFacade = mapFacade;
        this.gameConfig = gameConfig;
        this.backgroundScroller = backgroundScroller;
        this.gamePanel = gamePanel;
        this.attacksContainer = attacksContainer;
        this.enemiesWrapper = enemiesWrapper;
        this.gelaberOverlay = gelaberOverlay;
    }

    @Override
    public ControllerInterface keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_A -> manfred.left();
            case KeyEvent.VK_D -> manfred.right();
            case KeyEvent.VK_S -> manfred.down();
            case KeyEvent.VK_W -> manfred.up();
            case KeyEvent.VK_SPACE -> attackCaster.cast(manfred.getSprite(), manfred.getDirection());
            default -> CombinationElement.fromKeyEvent(event).ifPresent(attackCaster::addToCombination);
        }
        return this;
    }

    @Override
    public ControllerInterface keyReleased(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_A, KeyEvent.VK_D -> {
                manfred.stopX();
                manfred.checkForVerticalViewDirection();
            }
            case KeyEvent.VK_S, KeyEvent.VK_W -> {
                manfred.stopY();
                manfred.checkForHorizontalViewDirection();
            }
            case KeyEvent.VK_ENTER -> {
                Point interactionMapTile = manfred.getInteractionMapTile();
                return mapFacade.getMap().getInteractable(interactionMapTile).interact().apply(this);
            }
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
        ControllerInterface newControllerState = mapFacade.getMap().stepOn(this.manfred.moveTo()).apply(this);

        enemiesWrapper.getEnemies().forEach(enemy -> enemy.move(manfred));
        attacksContainer.forEach(Attack::move);

        enemiesWrapper.getEnemies().forEach(
            enemy -> attacksContainer.forEach(
                attack -> attack.checkHit(enemy)
            )
        );
        enemiesWrapper.removeKilled();
        attacksContainer.removeResolved();

        return newControllerState;
    }

    public void loadMap(String name) {
        try {
            mapFacade.loadMap(name);
        } catch (InvalidInputException e) {
            System.out.println("ERROR: Failed to load map " + name + "\n");
            e.printStackTrace();
        }
    }

    public void resetManfredPositionTo(int x, int y) {
        this.manfred.setX(this.gameConfig.getPixelBlockSize() * x);
        this.manfred.setY(this.gameConfig.getPixelBlockSize() * y);
        this.backgroundScroller.centerTo(this.manfred.getSprite().getCenter());
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
