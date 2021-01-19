package manfred.data.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import manfred.data.InvalidInputException;
import manfred.data.ObjectReader;
import manfred.data.helper.UrlHelper;
import manfred.data.image.ImageLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.function.Supplier;

@Component
public class PersonReader implements ObjectReader<PersonDto> {
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
            urlHelper.getResourceForPerson(name).orElseThrow(invalidInputException("Resource for person " + name + " not found")),
            urlHelper.getImageResourceForPerson(name).orElseThrow(invalidInputException("Image resource for person " + name + " not found"))
        );
    }

    private Supplier<InvalidInputException> invalidInputException(String message) {
        return () -> new InvalidInputException(message);
    }

    PersonDto load(URL yamlURL, URL imageURL) throws InvalidInputException {
        try {
            PersonDto personDto = objectMapper.readValue(yamlURL, PersonDto.class);
            personDto.setImage(imageLoader.load(imageURL));
            return personDto;
        } catch (IOException e) {
            throw new InvalidInputException(e);
        }
    }
}
