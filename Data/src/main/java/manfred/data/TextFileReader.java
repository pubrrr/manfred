package manfred.data;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
public class TextFileReader {

    public String read(String fileLocation) throws InvalidInputException {
        try {
            List<String> input = Files.readAllLines(Paths.get(fileLocation));
            return String.join("\n", input);
        } catch (IOException e) {
            throw new InvalidInputException(e);
        }
    }
}
