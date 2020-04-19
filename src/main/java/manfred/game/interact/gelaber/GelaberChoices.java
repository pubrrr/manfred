package manfred.game.interact.gelaber;

import java.util.HashMap;

public class GelaberChoices extends AbstractGelaberText {
    HashMap<String, AbstractGelaberText> choices;

    public GelaberChoices(String[] lines, HashMap<String, AbstractGelaberText> choices) {
        this.lines = lines;
        this.choices = choices;
    }

    public HashMap<String, AbstractGelaberText> getChoices() {
        return choices;
    }
}
