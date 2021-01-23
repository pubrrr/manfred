package manfred.data.infrastructure.person.gelaber;

import java.util.Map;

public class ValidatedGelaberDtoBuilder {
    private final Map<String, TextId> keyToIdMap;

    public ValidatedGelaberDtoBuilder(Map<String, TextId> keyToIdMap) {
        this.keyToIdMap = keyToIdMap;
    }
}
