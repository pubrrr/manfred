package helpers;

import manfred.game.interact.person.GelaberEdge;
import manfred.game.interact.person.GelaberNodeIdentifier;

public class GelaberEdgeHelper {

    public static GelaberEdge edge(String id) {
        return GelaberEdge.continuingWith(new GelaberNodeIdentifier(id), "edgeTextFor" + id);
    }
}
