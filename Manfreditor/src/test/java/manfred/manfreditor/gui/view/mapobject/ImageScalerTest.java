package manfred.manfreditor.gui.view.mapobject;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class ImageScalerTest {

    private ImageScaler underTest;

    @BeforeEach
    void setUp() {
        underTest = new ImageScaler();
    }

    @Test
    void imageAlreadyHasDesiredSize() {
        ImageData input = new ImageData(100, 99, 1, new PaletteData());

        ImageData result = underTest.scaleToFitIn(new Point(100, 99)).apply(input);

        assertThat(result.width, is(100));
        assertThat(result.height, is(99));
    }

    @Test
    void imageHasDesiredWidthButSmallerHeight() {
        ImageData input = new ImageData(100, 5, 1, new PaletteData());

        ImageData result = underTest.scaleToFitIn(new Point(100, 99)).apply(input);

        assertThat(result.width, is(100));
        assertThat(result.height, is(5));
    }

    @Test
    void imageHasDesiredHeightButSmallerWidth() {
        ImageData input = new ImageData(5, 99, 1, new PaletteData());

        ImageData result = underTest.scaleToFitIn(new Point(100, 99)).apply(input);

        assertThat(result.width, is(5));
        assertThat(result.height, is(99));
    }

    @Test
    void imageHasSameRatioButIsSmaller() {
        ImageData input = new ImageData(10, 15, 1, new PaletteData());

        ImageData result = underTest.scaleToFitIn(new Point(100, 150)).apply(input);

        assertThat(result.width, is(100));
        assertThat(result.height, is(150));
    }

    @Test
    void smallerHorizontalRectangle() {
        ImageData input = new ImageData(80, 16, 1, new PaletteData());

        ImageData result = underTest.scaleToFitIn(new Point(100, 150)).apply(input);

        assertThat(result.width, is(100));
        assertThat(result.height, is(20));
    }

    @Test
    void smallerVerticalRectangle() {
        ImageData input = new ImageData(80, 120, 1, new PaletteData());

        ImageData result = underTest.scaleToFitIn(new Point(100, 150)).apply(input);

        assertThat(result.width, is(100));
        assertThat(result.height, is(150));
    }

    @Test
    void largerHorizontalRectangle() {
        ImageData input = new ImageData(800, 16, 1, new PaletteData());

        ImageData result = underTest.scaleToFitIn(new Point(100, 150)).apply(input);

        assertThat(result.width, is(100));
        assertThat(result.height, is(2));
    }

    @Test
    void largerVerticalRectangle() {
        ImageData input = new ImageData(80, 1200, 1, new PaletteData());

        ImageData result = underTest.scaleToFitIn(new Point(100, 150)).apply(input);

        assertThat(result.width, is(10));
        assertThat(result.height, is(150));
    }
}