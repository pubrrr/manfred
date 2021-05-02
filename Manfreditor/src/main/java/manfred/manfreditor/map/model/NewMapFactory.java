package manfred.manfreditor.map.model;

import lombok.AllArgsConstructor;
import manfred.data.persistence.reader.MapSource;
import manfred.data.persistence.reader.UrlHelper;
import manfred.data.shared.PositiveInt;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NewMapFactory {

    private final UrlHelper urlHelper;

    public Map create(String name, PositiveInt columns, PositiveInt rows) {
        return new Map(name, columns, rows, new MapSource(urlHelper.getFileForMap(name)));
    }
}
