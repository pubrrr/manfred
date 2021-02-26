package manfred.data.persistence.reader;

import lombok.Value;

import java.net.URL;

@Value
public class AttackSource {
    URL attackUrl;
    URL imageDirectoryUrl;
}
