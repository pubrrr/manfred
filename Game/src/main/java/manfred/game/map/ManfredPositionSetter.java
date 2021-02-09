package manfred.game.map;

import lombok.AllArgsConstructor;
import manfred.data.shared.PositiveInt;
import manfred.game.characters.Manfred;

@AllArgsConstructor
public class ManfredPositionSetter {
    private final Manfred manfred;
    private final PositiveInt targetSpawnX;
    private final PositiveInt targetSpawnY;

    public void resetManfredOnMap(Map map) {
        this.manfred.setToTile(map.tileAt(targetSpawnX, targetSpawnY));
    }
}
