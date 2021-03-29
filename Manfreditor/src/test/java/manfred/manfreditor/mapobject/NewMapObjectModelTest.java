package manfred.manfreditor.mapobject;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

class NewMapObjectModelTest {

    private NewMapObjectModel underTest;

    @BeforeEach
    void setUp() {
        underTest = new NewMapObjectModel();
    }

    @Test
    void getResultOnNewSessionFails() {
        Validation<Seq<String>, NewMapObjectData> result = underTest.getResult();

        assertThat(result.isValid(), is(false));
        assertThat(result.getError(), containsInAnyOrder("no name given", "no image data given"));
    }

    @Test
    void getResultWhenAllDataSetIsSuccessful() {
        ImageData imageData = someImageData();

        underTest.setName("name");
        underTest.setImageData(imageData);
        Validation<Seq<String>, NewMapObjectData> result = underTest.getResult();

        assertThat(result.isValid(), is(true));
        assertThat(result.get().getName(), is("name"));
        assertThat(result.get().getImageData(), is(imageData));
    }

    @Test
    void resettingTheSessionRestoresInitialError() {
        ImageData imageData = someImageData();
        Validation<Seq<String>, NewMapObjectData> initialResult = underTest.getResult();
        assertThat(initialResult.isValid(), is(false));

        underTest.setName("name");
        underTest.setImageData(imageData);
        Validation<Seq<String>, NewMapObjectData> validResult = underTest.getResult();
        assertThat(validResult.isValid(), is(true));

        underTest.newSession();
        Validation<Seq<String>, NewMapObjectData> resultAfterReset = underTest.getResult();
        assertThat(resultAfterReset.isValid(), is(false));
        assertThat(resultAfterReset.getError(), equalTo(initialResult.getError()));
    }

    private ImageData someImageData() {
        return new ImageData(1, 1, 1, new PaletteData(0, 0, 0));
    }
}