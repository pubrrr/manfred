package manfred.data.persistence.reader;

import lombok.Value;

import java.io.File;

@Value
public class MapTileSource {
    File yamlFile;
    File imageFile;
}
