package helpers;

import manfred.game.interact.person.gelaber.GelaberEdge;
import manfred.game.interact.person.gelaber.GelaberNodeIdentifier;

public class GelaberEdgeHelper {

    public static GelaberEdge edge(String id) {
        return GelaberEdge.continuingWith(new GelaberNodeIdentifier(id), "edgeTextFor" + id);
    }
}
