package manfred.game.attack;

import manfred.game.characters.Direction;
import manfred.game.characters.Sprite;
import manfred.game.graphics.paintable.Paintable;

public interface CastMode extends Paintable {

    CastMode cast(Sprite sprite, Direction viewDirection);

    CastMode off();

    void addToCombination(CombinationElement combinationElement);
}
