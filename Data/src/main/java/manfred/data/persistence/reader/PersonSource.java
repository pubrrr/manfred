package manfred.data.persistence.reader;

import lombok.Value;

import java.net.URL;

@Value
public class PersonSource {
    URL personSource;
    URL imageSource;
}
