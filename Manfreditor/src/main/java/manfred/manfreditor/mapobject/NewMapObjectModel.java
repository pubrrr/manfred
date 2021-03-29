package manfred.manfreditor.mapobject;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import org.eclipse.swt.graphics.ImageData;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class NewMapObjectModel {

    private String name;
    private ImageData imageData;

    public void newSession() {
        this.name = null;
        this.imageData = null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageData(ImageData imageData) {
        this.imageData = imageData;
    }

    public Optional<ImageData> getImageData() {
        return Optional.ofNullable(imageData);
    }

    public Validation<Seq<String>, NewMapObjectData> getResult() {
        return Validation
            .combine(
                name == null ? Validation.invalid("no name given") : Validation.valid(this.name),
                imageData == null ? Validation.invalid("no image data given") : Validation.valid(this.imageData)
            )
            .ap(NewMapObjectData::new);
    }
}
