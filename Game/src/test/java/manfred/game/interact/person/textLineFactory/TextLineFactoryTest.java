package manfred.game.interact.person.textLineFactory;

import manfred.game.interact.person.gelaber.GelaberNode;
import manfred.game.interact.person.gelaber.TextLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalToObject;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TextLineFactoryTest {

    private TextLineFactory underTest;

    private FactoryRule ruleMock;

    @BeforeEach
    void setUp() {
        ruleMock = mock(FactoryRule.class);
        underTest = new TextLineFactory(ruleMock);
    }

    @Test
    void create() {
        FactoryAction factoryActionMock = mock(FactoryAction.class);
        TextLine textLineMock = mock(TextLine.class);

        when(ruleMock.applicableTo(any())).thenReturn(Optional.of(factoryActionMock));
        when(factoryActionMock.create(any(), any())).thenReturn(textLineMock);

        TextLine textLine = underTest.create(new GelaberNode(List.of()), List.of());

        assertThat(textLine, equalToObject(textLineMock));
    }

    @Test
    void create_noMatchingStrategy() {
        when(ruleMock.applicableTo(any())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> underTest.create(new GelaberNode(List.of()), List.of()));
        assertThat(exception.getMessage(), containsString("No matching text line creation strategy found"));
    }
}