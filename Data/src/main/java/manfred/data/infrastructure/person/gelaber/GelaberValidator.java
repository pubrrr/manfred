package manfred.data.infrastructure.person.gelaber;

import manfred.data.InvalidInputException;
import manfred.data.persistence.dto.GelaberDto;

public class GelaberValidator {

    public ValidatedGelaberDto validate(GelaberDto gelaberDto) throws InvalidInputException {
//        Map<String, TextId> keyToIdMap = gelaberDto.getTexts().keySet().stream().collect(Collectors.toMap(s -> s, TextId::new));

//        ValidatedGelaberDto.buildWithIds(keyToIdMap);

        return null;
    }
}
