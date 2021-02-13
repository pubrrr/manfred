package manfred.infrastructureadapter.attack;

import manfred.data.InvalidInputException;
import manfred.data.infrastructure.ObjectConverter;
import manfred.data.persistence.dto.AttackDto;
import manfred.game.attack.AttackGenerator;
import manfred.game.characters.sprite.AnimatedSprite;
import manfred.game.characters.sprite.AnimatedSpriteCloneFactory;
import org.springframework.stereotype.Component;

@Component
public class AttackGeneratorConverter implements ObjectConverter<AttackDto, AttackGenerator> {

    public AttackGenerator convert(AttackDto attackDto) throws InvalidInputException {
        return new AttackGenerator(
            attackDto.getSpeed(),
            attackDto.getSizeX(),
            attackDto.getSizeY(),
            attackDto.getDamage(),
            attackDto.getRange(),
            buildSpriteFactory(attackDto)
        );
    }

    private AnimatedSpriteCloneFactory buildSpriteFactory(AttackDto attackDto) {
        double distancePerImage = (double) attackDto.getRange().value() / attackDto.getNumberOfAnimationImages().value();
        int timePerImage = (int) Math.round( distancePerImage / attackDto.getSpeed().value());

        return new AnimatedSprite(
            attackDto.getSizeX(),
            attackDto.getSizeY(),
            attackDto.getAttackAnimation(),
            timePerImage
        );
    }
}
