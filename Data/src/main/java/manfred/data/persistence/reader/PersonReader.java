package manfred.data.persistence.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import manfred.data.InvalidInputException;
import manfred.data.persistence.ObjectReader;
import manfred.data.persistence.dto.PersonDto;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.function.Supplier;

@Component
public class PersonReader implements ObjectReader<PersonSource, PersonDto> {
    private final ImageLoader imageLoader;
    private final ObjectMapper objectMapper;
    private final UrlHelper urlHelper;

    public PersonReader(ImageLoader imageLoader, ObjectMapper objectMapper, UrlHelper urlHelper) {
        this.imageLoader = imageLoader;
        this.objectMapper = objectMapper;
        this.urlHelper = urlHelper;
    }

    @Override
    public PersonDto load(String name) throws InvalidInputException {
        return load(
            urlHelper.getFileForPerson(name),
            urlHelper.getImageResourceForPerson(name).orElseThrow(invalidInputException("Image resource for person " + name + " not found"))
        );
    }

    @Override
    public PersonDto load(PersonSource personSource) throws InvalidInputException {
        return load(new File(personSource.getPersonSource().getFile()), personSource.getImageSource());
    }

    private Supplier<InvalidInputException> invalidInputException(String message) {
        return () -> new InvalidInputException(message);
    }

    PersonDto load(File yamlFile, URL imageURL) throws InvalidInputException {
        try {
            PersonDto personDto = objectMapper.readValue(yamlFile, PersonDto.class);
            personDto.setImage(imageLoader.load(imageURL));
            return personDto;
        } catch (IOException e) {
            throw new InvalidInputException(e);
        }
    }
}
