package manfred.game.interact.gelaber;

import manfred.game.exception.InvalidInputException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GelaberReader {
    public Gelaber convert(JSONArray jsonGelaber) throws InvalidInputException {
        try {
            TextLine[] textLines = new TextLine[jsonGelaber.length()];
            for (int i = 0; i < jsonGelaber.length(); i++) {
                JSONObject jsonTextLine = jsonGelaber.getJSONObject(i);
                textLines[i] = convertTextLine(jsonTextLine);
            }
            return new Gelaber(textLines);
        } catch (JSONException $e) {
            throw new InvalidInputException($e.getMessage());
        }
    }

    private TextLine convertTextLine(JSONObject jsonTextLine) throws InvalidInputException {
        String text = jsonTextLine.getString("text");
        GelaberType type;
        try {
            type = GelaberType.valueOf(jsonTextLine.getString("type"));
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException(e.getMessage());
        }
        return new TextLine(text, type);
    }
}
