package manfred.game.interact.gelaber;

import manfred.game.exception.InvalidInputException;
import org.json.JSONArray;
import org.json.JSONException;

public class GelaberReader {
    public Gelaber convert(JSONArray jsonGelaber) throws InvalidInputException {
        try {
            return new Gelaber();
        } catch (JSONException $e) {
            throw new InvalidInputException($e.getMessage());
        }
    }
}
