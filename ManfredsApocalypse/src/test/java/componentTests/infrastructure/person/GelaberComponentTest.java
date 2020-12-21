package componentTests.infrastructure.person;

import componentTests.TestGameContext;
import helpers.TestGameConfig;
import manfred.game.exception.ManfredException;
import manfred.game.interact.person.GelaberFacade;
import manfred.infrastructure.person.GelaberConverter;
import manfred.infrastructure.person.GelaberDto;
import manfred.infrastructure.person.GelaberTextDto;
import manfred.infrastructure.person.ReferenceDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitConfig(TestGameContext.class)
public class GelaberComponentTest {
    private final static int CHARACTERS_PER_LINE = 5;

    @Autowired
    private GelaberConverter underTest;

    @Autowired
    private TestGameConfig gameConfig;

    @BeforeEach
    void setUp() {
        gameConfig.withCharactersPerGelaberLine(CHARACTERS_PER_LINE);
    }

    @Test
    void oneText_selfReferencing() throws ManfredException {
        HashMap<String, String> oneSelfReferencingText = new HashMap<>();
        oneSelfReferencingText.put("singleEntry", "singleEntry");
        GelaberDto input = setupGelaberDto(oneSelfReferencingText, "singleEntry");

        GelaberFacade result = underTest.convert(input);

        assertNotNull(result);
    }

    @Test
    void twoTexts() throws ManfredException {
        HashMap<String, String> texts = new HashMap<>();
        texts.put("firstEntry", "secondEntry");
        texts.put("secondEntry", "firstEntry");
        GelaberDto input = setupGelaberDto(texts, "firstEntry");

        GelaberFacade result = underTest.convert(input);

        assertNotNull(result);
    }

    private GelaberDto setupGelaberDto(HashMap<String, String> keyToReferenceMap, String initialReference) {
        Map<String, GelaberTextDto> texts = keyToReferenceMap.entrySet().stream()
            .map(keyToReference -> {
                ReferenceDto referenceDto = new ReferenceDto();
                referenceDto.setTo(keyToReference.getValue());

                GelaberTextDto gelaberTextDto = new GelaberTextDto();
                gelaberTextDto.setText("some text");
                gelaberTextDto.setRef(referenceDto);

                return Map.entry(keyToReference.getKey(), gelaberTextDto);
            })
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        GelaberDto input = new GelaberDto();
        input.setInitialTextReference(initialReference);
        input.setTexts(texts);
        return input;
    }
}
