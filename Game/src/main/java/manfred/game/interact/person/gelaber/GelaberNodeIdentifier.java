package manfred.game.interact.person.gelaber;

import lombok.Value;
import manfred.data.infrastructure.person.gelaber.GelaberPrototype;

@Value
public class GelaberNodeIdentifier {
    String identifier;

    public static GelaberNodeIdentifier of(GelaberPrototype.TextId referencedId) {
        return new GelaberNodeIdentifier(referencedId.getId());
    }
}
