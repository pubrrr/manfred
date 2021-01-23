package manfred.data.infrastructure.person;

import manfred.data.InvalidInputException;
import manfred.data.persistence.dto.GelaberDto;
import manfred.data.infrastructure.person.gelaber.GelaberValidator;
import manfred.data.persistence.dto.PersonDto;
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

        LocatedPersonDtoBuilder result = underTest.convert(new PersonDto("name", gelaberDtoMock, imageMock));

        LocatedPersonDto locatedPersonDto = result.at(1, 2);

        assertThat(locatedPersonDto.getName(), is("name"));
        assertThat(locatedPersonDto.getImage(), is(imageMock));
        assertThat(locatedPersonDto.getPositionX(), is(1));
        assertThat(locatedPersonDto.getPositionY(), is(2));
        verify(gelaberValidatorMock).validate(gelaberDtoMock);
    }
}