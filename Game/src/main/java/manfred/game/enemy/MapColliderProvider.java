package manfred.game.enemy;

import manfred.data.InvalidInputException;
import manfred.game.characters.MapCollider;
import org.springframework.stereotype.Component;

@Component
public class MapColliderProvider {
    // this is not beautiful, bot otherwise one gets a cyclic dependency when creating the EnemyReader
    public MapCollider provide() throws InvalidInputException {
        return MapCollider.getInstance();
    }
}
