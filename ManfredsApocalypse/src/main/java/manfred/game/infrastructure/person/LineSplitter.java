package manfred.game.infrastructure.person;

import org.springframework.lang.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class LineSplitter {

    private final int charactersPerLine;

    public LineSplitter(int charactersPerLine) {
        this.charactersPerLine = charactersPerLine;
    }

    public List<String> splitIntoTextLinesFittingIntoTextBox(String wholeText) {
        return Arrays.stream(wholeText.split("\\R"))
            .map(this::splitLineIntoLines)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    private List<String> splitLineIntoLines(String wholeLine) {
        LinkedList<String> originalWords = new LinkedList<>(Arrays.asList(wholeLine.trim().split("\\s+")));

        LinkedList<String> words = cutWordsThatAreTooLongForOneLine(originalWords);

        List<String> result = new ArrayList<>();

        StringBuilder currentLineBuilder = new StringBuilder(charactersPerLine);
        String currentWord = words.poll();
        while (!words.isEmpty()) {
            if (fitsCombinedIntoOneLine(currentLineBuilder, currentWord)) {
                currentLineBuilder.append(currentWord).append(" ");
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

        return result;
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
