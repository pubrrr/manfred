package manfred.game.graphics.scrolling;

import com.tngtech.junit.dataprovider.DataProvider;
import com.tngtech.junit.dataprovider.DataProviderExtension;
import com.tngtech.junit.dataprovider.UseDataProvider;
import com.tngtech.junit.dataprovider.UseDataProviderExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(DataProviderExtension.class)
@ExtendWith(UseDataProviderExtension.class)
class BoundedDistanceTest {

    private BoundedDistance.Factory underTest;

    @BeforeEach
    void setUp() {
        this.underTest = BoundedDistance.factory(new Interval(0, 5));
    }

    @TestTemplate
    @UseDataProvider("provideValueAndExpectation")
    void contains(int value, int expectedValue) {
        BoundedDistance result = underTest.createOfWithinBounds(value);

        assertThat(result.value(), is(expectedValue));
    }

    @DataProvider
    static Object[][] provideValueAndExpectation() {
        return new Object[][]{
            {0, 0}, // lower bound
            {5, 5}, // upper bound
            {6, 5}, // too high
            {10, 5}, // too high
            {-1, 0}, // too low
            {-2, 0}, // too low
            {-6, 0}, // too low
            {2, 2}, // in range
            {3, 3}, // in range
        };
    }
}