package manfred.game.controls;

import lombok.AllArgsConstructor;
import manfred.game.attack.Attack;
import manfred.game.attack.AttacksContainer;
import manfred.game.characters.Manfred;
import manfred.game.characters.MapCollider;
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
    private final MapCollider mapCollider;

    public ControllerStateMapper<ManfredController, ControllerInterface> move() {
        this.manfred.move();
        ControllerStateMapper<ManfredController, ControllerInterface> newControllerState = mapFacade.stepOn(this.manfred.getCenterMapTile());

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
}
