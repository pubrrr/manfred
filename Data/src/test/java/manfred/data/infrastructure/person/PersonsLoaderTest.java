package manfred.data.infrastructure.person;

import manfred.data.InvalidInputException;
import manfred.data.persistence.dto.MapPersonDto;
import manfred.data.infrastructure.person.gelaber.ValidatedGelaberDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PersonsLoaderTest {

    private PersonsLoader underTest;
    private PersonProvider personProviderMock;

    @BeforeEach
    void setUp() {
        personProviderMock = mock(PersonProvider.class);
        underTest = new PersonsLoader(personProviderMock);
    }

    @Test
    void emptyInput() throws InvalidInputException {
        List<LocatedPersonDto> result = underTest.load(List.of());

        assertThat(result, empty());
    }

    @Test
    void oneValidInput() throws InvalidInputException {
        when(personProviderMock.provide(any())).thenReturn(new LocatedPersonDtoBuilder("name", new ValidatedGelaberDto(), null));

        int positionX = 5;
        int positionY = 10;
        List<LocatedPersonDto> result = underTest.load(List.of(new MapPersonDto("name", positionX, positionY)));

        assertThat(result, hasSize(1));
        LocatedPersonDto locatedPersonDto = result.get(0);
        assertThat(locatedPersonDto.getPositionX(), is(positionX));
        assertThat(locatedPersonDto.getPositionY(), is(positionY));
    }

    @Test
    void twoValidInputs() throws InvalidInputException {
        when(personProviderMock.provide(any())).thenReturn(new LocatedPersonDtoBuilder("name", new ValidatedGelaberDto(), null));

        List<LocatedPersonDto> result = underTest.load(List.of(
            new MapPersonDto("name", 1, 2),
            new MapPersonDto("name", 1, 2)
        ));

        assertThat(result, hasSize(2));
    }

    @Test
    void oneValidOneInvalidInput() throws InvalidInputException {
        when(personProviderMock.provide(same("valid"))).thenReturn(new LocatedPersonDtoBuilder("name", new ValidatedGelaberDto(), null));
        when(personProviderMock.provide(same("invalid"))).thenThrow(new InvalidInputException("personProviderEnemyMessage"));

        List<MapPersonDto> input = List.of(
            new MapPersonDto("valid", 1, 2),
            new MapPersonDto("invalid", 1, 2)
        );

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> underTest.load(input));
        assertThat(exception.getMessage(), containsString("personProviderEnemyMessage"));
    }

    @Test
    void twoInvalidInputs() throws InvalidInputException {
        when(personProviderMock.provide(same("invalid1"))).thenThrow(new InvalidInputException("invalid1"));
        when(personProviderMock.provide(same("invalid2"))).thenThrow(new InvalidInputException("invalid2"));

        List<MapPersonDto> input = List.of(
            new MapPersonDto("invalid1", 1, 2),
            new MapPersonDto("invalid2", 1, 2)
        );

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> underTest.load(input));
        assertThat(exception.getMessage(), containsString("invalid1,\ninvalid2"));
    }

}