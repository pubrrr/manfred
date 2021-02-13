package manfred.game.characters.sprite;

import manfred.data.shared.PositiveInt;

import java.awt.image.BufferedImage;
import java.util.List;

public class AnimatedSprite extends Sprite implements AnimatedSpriteCloneFactory {

    private final PositiveInt width;
    private final PositiveInt height;
    private final List<BufferedImage> attackAnimation;
    private final int timePerImage;

    private int framesCounter = 0;
    private int animationIdx = 0;

    public AnimatedSprite(PositiveInt width, PositiveInt height, List<BufferedImage> attackAnimation, int timePerImage) {
        this.width = width;
        this.height = height;
        this.attackAnimation = attackAnimation;
        this.timePerImage = timePerImage;
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
        return attackAnimation.get(animationIdx);
    }

    public void tick() {
        framesCounter++;
        if (framesCounter >= timePerImage) {
            framesCounter = 0;
            if (animationIdx + 1 < attackAnimation.size()) {
                animationIdx++;
            }
        }
    }

    @Override
    public AnimatedSprite buildClone() {
        return new AnimatedSprite(this.width, this.height, this.attackAnimation, this.timePerImage);
    }
}
