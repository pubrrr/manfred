package manfred.data.infrastructure.person.gelaber;

import java.util.Map;

public class ValidatedGelaberDto {
    public static ValidatedGelaberDtoBuilder buildWithIds(Map<String, TextId> keyToIdMap) {
        return new ValidatedGelaberDtoBuilder(keyToIdMap);
    }
}
