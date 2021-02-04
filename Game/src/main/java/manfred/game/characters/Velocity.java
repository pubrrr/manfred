package manfred.game.characters;

import manfred.data.shared.PositiveInt;
import manfred.game.map.Vector;

public class Velocity {

    private final PositiveInt speed;
    private final Vector directionVector;

    public Velocity(PositiveInt speed) {
        this.speed = speed;
        this.directionVector = Vector.zero();
    }

    private Velocity(PositiveInt speed, Vector directionVector) {
        this.speed = speed;
        this.directionVector = directionVector;
    }

    public Velocity accelerate(Direction direction) {
        if (this.directionVector.scalarProduct(direction.getVector()) > 0) {
            return this; // means the velocity already points in the desired direction
        }
        return new Velocity(
            this.speed,
            this.directionVector.add(direction.getVector())
        );
    }

    public Vector getVector() {
        return this.directionVector.length().toStrictlyPositive()
            .map(strictlyPositiveLength -> this.directionVector.scale(speed, strictlyPositiveLength))
            .orElse(this.directionVector);
    }
}
