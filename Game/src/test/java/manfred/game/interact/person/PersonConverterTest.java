package manfred.game.interact.person;

import manfred.data.InvalidInputException;
import manfred.data.person.GelaberDto;
import manfred.data.person.PersonDto;
import manfred.game.config.GameConfig;
import manfred.game.interact.person.gelaber.GelaberConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PersonConverterTest {

    private PersonConverter underTest;
    private GelaberConverter gelaberConverterMock;

    @BeforeEach
    void setUp() {
        gelaberConverterMock = mock(GelaberConverter.class);

        underTest = new PersonConverter(gelaberConverterMock, mock(GameConfig.class));
    }

    @Test
    void convert() throws InvalidInputException {
        PersonDto input = new PersonDto();
        input.setName("name");
        input.setGelaber(new GelaberDto());

        Person result = underTest.convert(input);

        assertThat(result, instanceOf(Person.class));
        assertThat(result.getName(), equalTo("name"));
        verify(gelaberConverterMock).convert(any());
    }
}