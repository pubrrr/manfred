package manfred.data.infrastructure.person.gelaber;

import lombok.Value;

import java.util.List;

@Value
public class GelaberTextPrototype {
    String text;
    List<ReferencePrototype> references;
}
