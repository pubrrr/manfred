package manfred.data.infrastructure.person.gelaber;

import lombok.Value;

@Value
public class ReferencePrototype {
    GelaberPrototype.TextId referencedId;
    String edgeText;
    boolean continueTalking;
}
