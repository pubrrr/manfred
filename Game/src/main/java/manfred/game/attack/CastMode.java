package manfred.game.attack;

import manfred.game.characters.Direction;
import manfred.game.characters.Sprite;
import manfred.game.graphics.paintable.LocatedPaintable;

public interface CastMode extends LocatedPaintable {

    CastMode cast(Sprite sprite, Direction viewDirection);

    CastMode off();

    void addToCombination(CombinationElement combinationElement);
}
