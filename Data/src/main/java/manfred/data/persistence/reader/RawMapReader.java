package manfred.data.persistence.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import manfred.data.InvalidInputException;
import manfred.data.persistence.PreviousFileContent;
import manfred.data.persistence.dto.RawMapDto;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.function.Supplier;

@Component
@AllArgsConstructor
public class RawMapReader {

    private final ObjectMapper objectMapper;
    private final UrlHelper urlHelper;

    public RawMapDto load(String name) throws InvalidInputException {
        URL yamlURL = urlHelper.getResourceForMap(name).orElseThrow(invalidInputException(name));

        return load(yamlURL);
    }

    public RawMapDto load(MapSource source) throws InvalidInputException {
        return load(source.getMapUrl());
    }

    RawMapDto load(URL yamlURL) throws InvalidInputException {
        try {
            return objectMapper.readValue(yamlURL, RawMapDto.class);
        } catch (IOException e) {
            throw new InvalidInputException("Could not read map from " + yamlURL, e);
        }
    }

    private Supplier<InvalidInputException> invalidInputException(String name) {
        return () -> new InvalidInputException("Did not find resource for map " + name);
    }

    public Try<Option<PreviousFileContent>> save(RawMapDto mapDto, File targetFile) {
        return Try.of(() -> targetFile.isFile() ? Files.readAllLines(targetFile.toPath()) : List.of(""))
            .map(lines -> String.join("\n", lines))
            .map(previousFileContent -> !previousFileContent.isEmpty()
                ? Option.some(new PreviousFileContent(previousFileContent))
                : Option.<PreviousFileContent>none())
            .andThenTry(() -> objectMapper.writeValue(targetFile, mapDto));
    }
}
