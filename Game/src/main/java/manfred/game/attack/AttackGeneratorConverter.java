package manfred.game.attack;

import manfred.data.InvalidInputException;
import manfred.data.ObjectConverter;
import manfred.data.attack.AttackDto;
import manfred.game.enemy.MapColliderProvider;
import org.springframework.stereotype.Component;

@Component
public class AttackGeneratorConverter implements ObjectConverter<AttackDto, AttackGenerator> {

    private final MapColliderProvider mapColliderProvider;

    public AttackGeneratorConverter(MapColliderProvider mapColliderProvider) {
        this.mapColliderProvider = mapColliderProvider;
    }

    public AttackGenerator convert(AttackDto attackDto) throws InvalidInputException {
        return new AttackGenerator(
            attackDto.getSpeed(),
            attackDto.getSizeX(),
            attackDto.getSizeY(),
            mapColliderProvider.provide(),
            attackDto.getDamage(),
            attackDto.getRange(),
            attackDto.getAttackAnimation(),
            attackDto.getNumberOfAnimationImages()
        );
    }
}
