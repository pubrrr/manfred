package manfred.data.infrastructure.person;

import manfred.data.InvalidInputException;
import manfred.data.persistence.dto.GelaberDto;
import manfred.data.infrastructure.person.gelaber.GelaberValidator;
import manfred.data.persistence.dto.PersonDto;
import manfred.data.shared.PositiveInt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PersonConverterTest {

    private PersonConverter underTest;
    private GelaberValidator gelaberValidatorMock;

    @BeforeEach
    void setUp() {
        gelaberValidatorMock = mock(GelaberValidator.class);
        underTest = new PersonConverter(gelaberValidatorMock);
    }

    @Test
    void test() throws InvalidInputException {
        BufferedImage imageMock = mock(BufferedImage.class);
        GelaberDto gelaberDtoMock = mock(GelaberDto.class);

        PersonPrototypeBuilder result = underTest.convert(new PersonDto("name", gelaberDtoMock, imageMock));

        PersonPrototype personPrototype = result.at(PositiveInt.of(1), PositiveInt.of(2));

        assertThat(personPrototype.getName(), is("name"));
        assertThat(personPrototype.getImage(), is(imageMock));
        assertThat(personPrototype.getPositionX(), is(PositiveInt.of(1)));
        assertThat(personPrototype.getPositionY(), is(PositiveInt.of(2)));
        verify(gelaberValidatorMock).validate(gelaberDtoMock);
    }
}