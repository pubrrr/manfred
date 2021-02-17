package manfred.game.characters.sprite;

import manfred.data.shared.PositiveInt;
import manfred.game.characters.Direction;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class DirectionalAnimatedSprite extends Sprite {

    private final PositiveInt.Strict width;
    private final PositiveInt.Strict height;
    private final HashMap<Direction, BufferedImage[]> animationImages;
    private final int nextAnimationTrigger;

    private int framesCounter = 0;
    private int animationPosition = 0;
    private Direction direction = Direction.DOWN;

    public DirectionalAnimatedSprite(PositiveInt.Strict width, PositiveInt.Strict height, HashMap<Direction, BufferedImage[]> animationImages, PositiveInt nextAnimationTrigger) {
        this.width = width;
        this.height = height;
        this.animationImages = animationImages;
        this.nextAnimationTrigger = nextAnimationTrigger.value();
    }

    public void stopAnimation() {
        this.framesCounter = 0;
        this.animationPosition = 0;
    }

    public void tick(Direction viewDirection) {
        this.direction = viewDirection;

        this.framesCounter++;
        if (this.framesCounter >= this.nextAnimationTrigger) {
            this.framesCounter = 0;
            this.animationPosition++;
            if (this.animationPosition >= this.animationImages.size()) {
                this.animationPosition = 0;
            }
        }
    }

    @Override
    public PositiveInt getHeight() {
        return this.height;
    }

    @Override
    public PositiveInt getWidth() {
        return this.width;
    }

    @Override
    public BufferedImage getImage() {
        return this.animationImages.get(this.direction)[this.animationPosition];
    }
}
