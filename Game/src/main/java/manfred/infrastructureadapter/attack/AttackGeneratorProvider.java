package manfred.infrastructureadapter.attack;

import manfred.data.ObjectProvider;
import manfred.data.persistence.dto.AttackDto;
import manfred.data.persistence.reader.AttackReader;
import manfred.data.persistence.reader.AttackSource;
import manfred.game.attack.AttackGenerator;
import org.springframework.stereotype.Component;

@Component
public class AttackGeneratorProvider extends ObjectProvider<AttackSource, AttackDto, AttackGenerator> {

    public AttackGeneratorProvider(AttackReader objectReader, AttackGeneratorConverter objectConverter) {
        super(objectReader, objectConverter);
    }
}
