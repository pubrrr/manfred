package manfred.game.graphics.scrolling;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InvertedScrollerTest {

    private InvertedScroller underTest;
    private CoordinateScroller scrollerMock;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        scrollerMock = mock(CoordinateScroller.class);
        underTest = new InvertedScroller(scrollerMock);
    }

    @Test
    void invert0() {
        when(scrollerMock.computeScrollDistance(anyInt())).thenReturn(0);

        assertThat(underTest.computeScrollDistance(5), is(0));
    }

    @Test
    void invert() {
        when(scrollerMock.computeScrollDistance(anyInt())).thenReturn(7);

        assertThat(underTest.computeScrollDistance(5), is(-7));
    }
}