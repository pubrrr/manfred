package manfred.infrastructureadapter.attack;

import manfred.data.InvalidInputException;
import manfred.data.infrastructure.ObjectConverter;
import manfred.data.persistence.dto.AttackDto;
import manfred.game.attack.AttackGenerator;
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
            attackDto.getAttackAnimation(),
            attackDto.getNumberOfAnimationImages()
        );
    }
}
