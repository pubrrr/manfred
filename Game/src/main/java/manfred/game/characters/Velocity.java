package manfred.game.characters;

import manfred.data.shared.PositiveInt;
import manfred.game.map.Vector;

public class Velocity {

    private final PositiveInt speed;
    private final Vector directionVector;

    private Velocity(PositiveInt speed, Vector directionVector) {
        this.speed = speed;
        this.directionVector = directionVector;
    }

    public static Velocity withSpeed(PositiveInt speed) {
        return new Velocity(speed, Vector.zero());
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

    public Velocity moveInDirection(Vector directionVector) {
        return new Velocity(this.speed, directionVector);
    }

    public Vector getVector() {
        return this.directionVector.length().toStrictlyPositive()
            .map(strictlyPositiveLength -> this.directionVector.scale(speed, strictlyPositiveLength))
            .orElse(this.directionVector);
    }

    public Velocity stopX() {
        return new Velocity(
            this.speed,
            Vector.of(0, this.directionVector.y())
        );
    }

    public Velocity stopY() {
        return new Velocity(
            this.speed,
            Vector.of(this.directionVector.x(), 0)
        );
    }

    public Velocity stop() {
        return new Velocity(this.speed, Vector.zero());
    }

    public PositiveInt getMaxSpeed() {
        return this.speed;
    }
}
