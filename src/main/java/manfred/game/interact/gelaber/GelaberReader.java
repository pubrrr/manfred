package manfred.game.interact.gelaber;

import manfred.game.GameConfig;
import manfred.game.exception.InvalidInputException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.lang.Nullable;

import java.util.*;

public class GelaberReader {
    private int charactersPerLine;
    private GameConfig gameConfig;

    public GelaberReader(GameConfig gameConfig) {
        this.charactersPerLine = gameConfig.getCharacterPerGelaberLine();
        this.gameConfig = gameConfig;
    }

    public Gelaber convert(JSONArray jsonGelaber) throws InvalidInputException {
        try {
            AbstractGelaberText[] texts = new AbstractGelaberText[jsonGelaber.length()];
            for (int i = 0; i < jsonGelaber.length(); i++) {
                JSONObject jsonTextLine = jsonGelaber.getJSONObject(i);
                texts[i] = convertText(jsonTextLine);
            }
            return new Gelaber(texts, gameConfig);
        } catch (JSONException $e) {
            throw new InvalidInputException($e.getMessage());
        }
    }

    private AbstractGelaberText convertText(JSONObject jsonTextLine) throws InvalidInputException {
        String wholeText = jsonTextLine.getString("text");
        String[] lines = splitIntoTextLinesFittingIntoTextBox(wholeText);
        GelaberType type;
        try {
            type = GelaberType.valueOf(jsonTextLine.getString("type"));
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException(e.getMessage());
        }

        if (type == GelaberType.gelaber) {
            return new GelaberText(lines, gameConfig);
        }

        HashMap<String, AbstractGelaberText> choices = convertChoices(jsonTextLine.getJSONObject("choices"));
        return new GelaberChoices(lines, choices, new SelectionMarker(gameConfig), gameConfig);
    }

    private HashMap<String, AbstractGelaberText> convertChoices(JSONObject choices) throws InvalidInputException {
        Set<String> keys = choices.keySet();

        HashMap<String, AbstractGelaberText> result = new HashMap<>();
        for (String key : keys) {
            if (Objects.equals(choices.get(key), null)) {
                result.put(key, null);
            } else {
                result.put(key, convertText(choices.getJSONObject(key)));
            }
        }
        return result;
    }

    private String[] splitIntoTextLinesFittingIntoTextBox(String wholeText) {
        LinkedList<String> originalWords = new LinkedList(Arrays.asList(wholeText.split("\\s+")));
        LinkedList<String> words = cutWordsThatAreTooLongForOneLine(originalWords);

        List<String> result = new ArrayList<>();

        StringBuilder currentLineBuilder = new StringBuilder(charactersPerLine);
        String currentWord = words.poll();
        while (!words.isEmpty()) {
            if (fitsCombinedIntoOneLine(currentLineBuilder, currentWord)) {
                currentLineBuilder.append(currentWord + " ");
                currentWord = words.poll();
            } else {
                result.add(currentLineBuilder.toString());
                currentLineBuilder = new StringBuilder(charactersPerLine);
            }
        }

        if (fitsCombinedIntoOneLine(currentLineBuilder, currentWord)) {
            currentLineBuilder.append(currentWord);
            result.add(currentLineBuilder.toString());
        } else {
            result.add(currentLineBuilder.toString());
            result.add(currentWord);
        }

        return result.toArray(new String[0]); // the argument is to tell the function to return String[] instead of Object[]
    }

    private LinkedList<String> cutWordsThatAreTooLongForOneLine(LinkedList<String> words) {
        for (int idx = 0; idx < words.size(); idx++) {
            String currentWord = words.get(idx);
            if (currentWord.length() > charactersPerLine) {
                words.remove(idx);
                words.add(idx, currentWord.substring(0, charactersPerLine));
                words.add(idx + 1, currentWord.substring(charactersPerLine));
            }
        }
        return words;
    }

    private boolean fitsCombinedIntoOneLine(StringBuilder currentLineBuilder, @Nullable String currentWord) {
        return currentWord == null || currentLineBuilder.length() + currentWord.length() <= charactersPerLine;
    }
}
