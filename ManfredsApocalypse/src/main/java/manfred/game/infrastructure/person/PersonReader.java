package manfred.game.infrastructure.person;

import manfred.game.Game;
import manfred.game.GameConfig;
import manfred.game.exception.InvalidInputException;
import manfred.game.exception.ManfredException;
import manfred.game.graphics.ImageLoader;
import manfred.game.interact.person.GelaberFacade;
import manfred.game.interact.person.Person;
import org.json.JSONException;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
public class PersonReader {
    public static final String PATH_PERSONS = Game.PATH_DATA + "persons\\";

    private final GelaberConverter gelaberConverter;
    private final GameConfig gameConfig;
    private final ImageLoader imageLoader;
    private final PersonDtoReader personDtoReader;

    public PersonReader(GelaberConverter gelaberConverter, GameConfig gameConfig, ImageLoader imageLoader, PersonDtoReader personDtoReader) {
        this.gelaberConverter = gelaberConverter;
        this.gameConfig = gameConfig;
        this.imageLoader = imageLoader;
        this.personDtoReader = personDtoReader;
    }

    public Person load(String name) throws ManfredException, IOException {
        String yaml = read(PATH_PERSONS + name + ".yaml");
        PersonDto personDto = personDtoReader.read(yaml);
        return convert(personDto);
    }

    String read(String jsonFileLocation) throws IOException {
        List<String> input = Files.readAllLines(Paths.get(jsonFileLocation));
        return String.join("\n", input);
    }

    Person convert(PersonDto personDto) throws ManfredException {
        try {
            String name = personDto.getName();
            GelaberFacade gelaberFacade = gelaberConverter.convert(personDto.getGelaber());
            BufferedImage image = imageLoader.load(PATH_PERSONS + name + ".png");

            return new Person(name, gelaberFacade, gameConfig, image);
        } catch (JSONException | IOException e) {
            throw new InvalidInputException(e.getMessage());
        }
    }
}
