package manfred.game.interact.person;

import manfred.game.GameConfig;
import org.junit.jupiter.api.Test;

import java.util.List;

import static helpers.GelaberEdgeHelper.edge;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToObject;
import static org.mockito.Mockito.mock;

class SelectorTest {

    @Test
    void oneEdge() {
        GelaberEdge edge = edge("first");
        Selector selector = Selector.fromEdges(List.of(edge), mock(GameConfig.class));

        assertThat(selector.confirm(), equalToObject(edge));
    }

    @Test
    void oneEdge_previous() {
        GelaberEdge edge = edge("first");
        Selector selector = Selector.fromEdges(List.of(edge), mock(GameConfig.class));

        selector.selectPrevious();
        assertThat(selector.confirm(), equalToObject(edge));
    }

    @Test
    void oneEdge_next() {
        GelaberEdge edge = edge("first");
        Selector selector = Selector.fromEdges(List.of(edge), mock(GameConfig.class));

        selector.selectNext();
        assertThat(selector.confirm(), equalToObject(edge));
    }

    @Test
    void twoEdges_previous() {
        GelaberEdge firstEdge = edge("first");
        GelaberEdge secondEdge = edge("second");
        Selector selector = Selector.fromEdges(List.of(firstEdge, secondEdge), mock(GameConfig.class));

        assertThat(selector.confirm(), equalToObject(firstEdge));

        selector.selectPrevious();
        assertThat(selector.confirm(), equalToObject(secondEdge));

        selector.selectPrevious();
        assertThat(selector.confirm(), equalToObject(firstEdge));
    }

    @Test
    void twoEdges_next() {
        GelaberEdge firstEdge = edge("first");
        GelaberEdge secondEdge = edge("second");
        Selector selector = Selector.fromEdges(List.of(firstEdge, secondEdge), mock(GameConfig.class));

        assertThat(selector.confirm(), equalToObject(firstEdge));

        selector.selectNext();
        assertThat(selector.confirm(), equalToObject(secondEdge));

        selector.selectNext();
        assertThat(selector.confirm(), equalToObject(firstEdge));
    }
}