package manfred.data.infrastructure.person.gelaber;

import manfred.data.InvalidInputException;
import manfred.data.persistence.dto.GelaberDto;
import org.springframework.stereotype.Component;

@Component
public class GelaberValidator {

    public GelaberPrototype validate(GelaberDto gelaberDto) throws InvalidInputException {
        return GelaberPrototype.builder()
            .WithTexts(gelaberDto.getTexts())
            .withInitialReference(gelaberDto.getInitialTextReference())
            .validateAndBuild();
    }
}
