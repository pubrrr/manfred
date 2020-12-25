package manfred.game.infrastructure.person;

import manfred.game.GameConfig;
import manfred.game.exception.ManfredException;
import manfred.game.graphics.ImageLoader;
import manfred.game.interact.person.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PersonReaderTest {
    private GelaberConverter gelaberConverterMock;
    private PersonReader underTest;
    private ImageLoader imageLoaderMock;

    @BeforeEach
    void init() {
        gelaberConverterMock = mock(GelaberConverter.class);
        imageLoaderMock = mock(ImageLoader.class);

        underTest = new PersonReader(gelaberConverterMock, mock(GameConfig.class), imageLoaderMock, mock(PersonDtoReader.class));
    }

    @Test
    void convertsName() throws ManfredException, IOException {
        PersonDto input = new PersonDto();
        input.setName("testName");

        Person result = underTest.convert(input);

        verify(imageLoaderMock).load("ManfredsApocalypse\\data\\persons\\testName.png");
        assertEquals("testName", result.getName());
    }

    @Test
    void triggersGelaberConversion() throws ManfredException {
        GelaberDto gelaberDtoMock = mock(GelaberDto.class);

        PersonDto input = new PersonDto();
        input.setName("testName");
        input.setGelaber(gelaberDtoMock);

        underTest.convert(input);

        verify(gelaberConverterMock).convert(gelaberDtoMock);
    }
}