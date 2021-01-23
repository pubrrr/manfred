package manfred.game.attack;

import manfred.data.ObjectProvider;
import manfred.data.persistence.dto.AttackDto;
import manfred.data.persistence.reader.AttackReader;
import org.springframework.stereotype.Component;

@Component
public class AttackGeneratorProvider extends ObjectProvider<AttackDto, AttackGenerator> {

    public AttackGeneratorProvider(AttackReader objectReader, AttackGeneratorConverter objectConverter) {
        super(objectReader, objectConverter);
    }
}
