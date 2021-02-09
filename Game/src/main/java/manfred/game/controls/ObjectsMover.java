package manfred.game.controls;

import lombok.AllArgsConstructor;
import manfred.game.attack.AttacksContainer;
import manfred.game.characters.Manfred;
import manfred.game.enemy.EnemiesWrapper;
import manfred.game.map.MapFacade;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ObjectsMover {

    private final MapFacade mapFacade;
    private final Manfred manfred;
    private final EnemiesWrapper enemiesWrapper;
    private final AttacksContainer attacksContainer;

    public ControllerStateMapper<ManfredController, ControllerInterface> move() {
        this.manfred.checkCollisionsAndMove(this.mapFacade);
        ControllerStateMapper<ManfredController, ControllerInterface> newControllerState = mapFacade.stepOn(this.manfred.getCenter().getTile());

        enemiesWrapper.getEnemies().forEach(enemy -> enemy.determineSpeed(manfred).checkCollisionsAndMove(this.mapFacade));
        attacksContainer.forEach(attack -> attack.checkCollisionsAndMove(this.mapFacade));

        enemiesWrapper.getEnemies().forEach(
            enemy -> attacksContainer.forEach(
                attack -> attack.checkHit(enemy)
            )
        );
        enemiesWrapper.removeKilled();
        attacksContainer.removeResolved();

        return newControllerState;
    }
}
