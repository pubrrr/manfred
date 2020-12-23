package manfred.game.interact.person;

import org.junit.jupiter.api.Test;

import java.util.List;

import static helpers.GelaberEdgeHelper.edge;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToObject;

class SelectorTest {

    @Test
    void oneEdge() {
        GelaberEdge edge = edge("first");
        Selector selector = Selector.fromEdges(List.of(edge));

        assertThat(selector.confirm(), equalToObject(edge.follow()));
    }

    @Test
    void oneEdge_previous() {
        GelaberEdge edge = edge("first");
        Selector selector = Selector.fromEdges(List.of(edge));

        selector.selectPrevious();
        assertThat(selector.confirm(), equalToObject(edge.follow()));
    }

    @Test
    void oneEdge_next() {
        GelaberEdge edge = edge("first");
        Selector selector = Selector.fromEdges(List.of(edge));

        selector.selectNext();
        assertThat(selector.confirm(), equalToObject(edge.follow()));
    }

    @Test
    void twoEdges_previous() {
        GelaberEdge firstEdge = edge("first");
        GelaberEdge secondEdge = edge("second");
        Selector selector = Selector.fromEdges(List.of(firstEdge, secondEdge));

        assertThat(selector.confirm(), equalToObject(firstEdge.follow()));

        selector.selectPrevious();
        assertThat(selector.confirm(), equalToObject(secondEdge.follow()));

        selector.selectPrevious();
        assertThat(selector.confirm(), equalToObject(firstEdge.follow()));
    }

    @Test
    void twoEdges_next() {
        GelaberEdge firstEdge = edge("first");
        GelaberEdge secondEdge = edge("second");
        Selector selector = Selector.fromEdges(List.of(firstEdge, secondEdge));

        assertThat(selector.confirm(), equalToObject(firstEdge.follow()));

        selector.selectNext();
        assertThat(selector.confirm(), equalToObject(secondEdge.follow()));

        selector.selectNext();
        assertThat(selector.confirm(), equalToObject(firstEdge.follow()));
    }
}