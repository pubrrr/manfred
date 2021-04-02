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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NewMapObjectModelTest {

    private NewMapObjectModel underTest;
    private ObjectAccessibilityValidator objectAccessibilityValidatorMock;

    @BeforeEach
    void setUp() {
        objectAccessibilityValidatorMock = mock(ObjectAccessibilityValidator.class);
        underTest = new NewMapObjectModel(objectAccessibilityValidatorMock);
    }

    @Test
    void getResultOnNewSessionFails() {
        when(objectAccessibilityValidatorMock.validate(any())).thenReturn(Validation.invalid("accessibility invalid message"));

        Validation<Seq<String>, NewMapObjectData> result = underTest.getResult();

        assertThat(result.isValid(), is(false));
        assertThat(result.getError(), containsInAnyOrder("no name given", "no image data given", "accessibility invalid message"));
    }

    @Test
    void getResultWhenAllDataSetIsSuccessful() {
        ImageData imageData = someImageData();
        AccessibilityGrid accessibilityGridMock = mock(AccessibilityGrid.class);
        when(objectAccessibilityValidatorMock.validate(any())).thenReturn(Validation.valid(accessibilityGridMock));

        underTest.setName("name");
        underTest.setImageData(imageData);
        Validation<Seq<String>, NewMapObjectData> result = underTest.getResult();

        assertThat(result.isValid(), is(true));
        assertThat(result.get().getName(), is("name"));
        assertThat(result.get().getImageData(), is(imageData));
        assertThat(result.get().getAccessibilityGrid(), is(accessibilityGridMock));
    }

    @Test
    void resettingTheSessionRestoresInitialError() {
        AccessibilityGrid accessibilityGridMock = mock(AccessibilityGrid.class);
        when(objectAccessibilityValidatorMock.validate(any())).thenReturn(Validation.valid(accessibilityGridMock));

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