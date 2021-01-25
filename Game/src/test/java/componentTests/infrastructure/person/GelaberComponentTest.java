package componentTests.infrastructure.person;

import componentTests.TestGameContext;
import helpers.TestGameConfig;
import manfred.data.InvalidInputException;
import manfred.data.infrastructure.person.gelaber.GelaberPrototype;
import manfred.data.infrastructure.person.gelaber.GelaberValidator;
import manfred.data.persistence.dto.GelaberDto;
import manfred.data.persistence.dto.GelaberTextDto;
import manfred.data.persistence.dto.ReferenceDto;
import manfred.game.interact.person.gelaber.GelaberConverter;
import manfred.game.interact.person.gelaber.GelaberFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static helpers.GelaberHelper.setupGelaber;
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
    void oneText_selfReferencing() throws InvalidInputException {
        HashMap<String, List<String>> oneSelfReferencingText = new HashMap<>();
        oneSelfReferencingText.put("singleEntry", List.of("singleEntry"));
        GelaberPrototype input = setupGelaber(oneSelfReferencingText, "singleEntry");

        GelaberFacade result = underTest.convert(input);

        assertNotNull(result);
    }

    @Test
    void twoTexts() throws InvalidInputException {
        HashMap<String, List<String>> texts = new HashMap<>();
        texts.put("firstEntry", List.of("secondEntry"));
        texts.put("secondEntry", List.of("firstEntry"));
        GelaberPrototype input = setupGelaber(texts, "firstEntry");

        GelaberFacade result = underTest.convert(input);

        assertNotNull(result);
    }
}
