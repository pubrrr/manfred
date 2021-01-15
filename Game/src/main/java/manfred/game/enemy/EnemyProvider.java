package manfred.game.enemy;

import manfred.data.ObjectProvider;
import manfred.data.enemy.EnemyDto;
import manfred.data.enemy.EnemyReader;
import org.springframework.stereotype.Component;

@Component
public class EnemyProvider extends ObjectProvider<EnemyDto, UnlocatedEnemy> {

    protected EnemyProvider(EnemyReader objectReader, EnemyConverter objectConverter) {
        super(objectReader, objectConverter);
    }
}
