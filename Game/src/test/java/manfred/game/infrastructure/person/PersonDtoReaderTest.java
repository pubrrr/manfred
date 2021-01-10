package manfred.game.infrastructure.person;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonDtoReaderTest {
    private PersonDtoReader underTest;

    @BeforeEach
    void setUp() {
        underTest = new PersonDtoReader(new ObjectMapper(new YAMLFactory()));
    }

    @Test
    void test() throws JsonProcessingException {
        String input = "---\n" +
            "name: Opa\n" +
            "gelaber:\n" +
            "  initialTextReference: Intro\n" +
            "  texts:\n" +
            "    Intro:\n" +
            "      text: Was soll ich dir erzählen, mein Sohn?\n" +
            "      references:\n" +
            "        - to: Nutten\n" +
            "          text: Nutten\n" +
            "          continueTalking: true\n" +
            "        - to: Koks\n" +
            "          text: Koks\n" +
            "          continueTalking: true\n" +
            "        - to: Nichts\n" +
            "          text: Nichts\n" +
            "          continueTalking: true\n" +
            "    Nutten:\n" +
            "      text: Früher war alles besser! Da hatten die Männer noch richtige Bärte! Und die\n" +
            "        Nutten hatten noch ordendlich was drauf. Nicht so wie heute, da ist ein anständiger\n" +
            "        Achselfick schon das höchste der Gefühle. Aber mein Lurchi braucht doch Liebe!\n" +
            "        Wegen meiner scheiss Artritis kann ich mir kaum noch einen keulen und mit der\n" +
            "        Achsel komm ich auch nicht mehr so weit runter. Und was unternimmt der Staat?\n" +
            "        Der weiss doch gar nicht mehr, wie eine Achselpolitik auszusehen hat. Was denkst\n" +
            "        du dazu, mein Junge?\n" +
            "      references:\n" +
            "        - to: Intro\n" +
            "          text: Ja\n" +
            "        - to: Intro\n" +
            "          text: Ja\n" +
            "    Koks:\n" +
            "      text: Pfui, mach sowas nicht!\n" +
            "      references:\n" +
            "        - to: Intro\n" +
            "    Nichts:\n" +
            "      text: Dann lass mich in Ruhe sterben!\n" +
            "      references:\n" +
            "        - to: Intro\n";

        PersonDto result = underTest.read(input);

        assertEquals("Opa", result.getName());
    }
}