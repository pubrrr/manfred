package componentTests.controller;

import com.google.common.collect.ImmutableBiMap;
import componentTests.TestGameContext;
import helpers.TestGameConfig;
import manfred.game.controls.ControllerInterface;
import manfred.game.controls.GelaberController;
import manfred.game.graphics.paintable.GelaberOverlay;
import manfred.game.interact.person.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@SpringJUnitConfig(TestGameContext.class)
public class GelaberControllerComponentTest extends ControllerTestCase {
    private static final int NUMBER_OF_TEXT_LINES = 5;

    @Autowired
    private GelaberFacadeBuilder gelaberFacadeBuilder;

    private GelaberController underTest;
    private ControllerInterface previous;
    private TestGameConfig testGameConfig;

    @BeforeEach
    void init() {
        previous = mock(ControllerInterface.class);
        testGameConfig = (new TestGameConfig()).withNumberOfTextLines(NUMBER_OF_TEXT_LINES);
    }

    @Test
    void nextLine_whenEnterPressed() {
        setupControllerWithGelaberText(
            Map.of("key", List.of(GelaberEdge.continuingWith(new GelaberNodeIdentifier("key"), "edgeText"))),
            "key"
        );

        ControllerInterface newControllerState = underTest.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));

        assertTrue(newControllerState instanceof GelaberController);
    }

    @Test
    void upAndDown() {
        setupControllerWithGelaberText(
            Map.of("key", List.of(GelaberEdge.continuingWith(new GelaberNodeIdentifier("key"), "edgeText"))),
            "key"
        );

        assertTrue(underTest.keyReleased(mockEventWithKey(KeyEvent.VK_UP)) instanceof GelaberController);
        assertTrue(underTest.keyReleased(mockEventWithKey(KeyEvent.VK_W)) instanceof GelaberController);

        assertTrue(underTest.keyReleased(mockEventWithKey(KeyEvent.VK_DOWN)) instanceof GelaberController);
        assertTrue(underTest.keyReleased(mockEventWithKey(KeyEvent.VK_S)) instanceof GelaberController);
    }

    @Test
    void returnsControlsToManfred_whenEnterPressedAndNoFurtherText() {
        setupControllerWithGelaberText(
            Map.of("key", List.of(GelaberEdge.abortingReferencingTo(new GelaberNodeIdentifier("key"), "edgeText"))),
            "key"
        );

        ControllerInterface newControllerState = underTest.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));

        assertSame(previous, newControllerState);
    }

    private void setupControllerWithGelaberText(Map<String, List<GelaberEdge>> graphMatrixProto, String initial) {
        Map<GelaberNodeIdentifier, List<GelaberEdge>> graphMatrix = graphMatrixProto.entrySet().stream()
            .collect(toMap(entry -> new GelaberNodeIdentifier(entry.getKey()), Map.Entry::getValue));

        ImmutableBiMap<GelaberNodeIdentifier, GelaberNode> gelaberNodes = graphMatrix.keySet().stream()
            .map(gelaberEdges -> Map.entry(gelaberEdges, new GelaberNode(new String[]{"some text for " + gelaberEdges})))
            .collect(collectingAndThen(toMap(Map.Entry::getKey, Map.Entry::getValue), ImmutableBiMap::copyOf));

        GelaberFacade gelaberFacade = this.gelaberFacadeBuilder
            .withGraphMatrix(new GelaberGraphMatrix(graphMatrix))
            .withNodes(gelaberNodes)
            .buildStartingAt(new GelaberNodeIdentifier(initial));

        underTest = new GelaberController(
            this.previous,
            gelaberFacade,
            mock(GelaberOverlay.class)
        );
    }
}
