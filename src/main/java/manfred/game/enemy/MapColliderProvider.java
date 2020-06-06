package manfred.game.enemy;

import manfred.game.characters.MapCollider;

public class MapColliderProvider {
    // this is not beautiful, bot otherwise one gets a cyclic dependency when creating the EnemyReader
    public MapCollider provide() throws Exception {
        return MapCollider.getInstance();
    }
}
