package manfred.game.attack;

import manfred.data.attack.AttackDto;
import manfred.game.enemy.MapColliderProvider;
import org.springframework.stereotype.Component;

@Component
public class AttackGeneratorConverter {

    private final MapColliderProvider mapColliderProvider;

    public AttackGeneratorConverter(MapColliderProvider mapColliderProvider) {
        this.mapColliderProvider = mapColliderProvider;
    }

    public AttackGenerator convert(AttackDto attackDto) throws Exception {
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
