package manfred.manfreditor.map;

import manfred.data.shared.PositiveInt;
import org.springframework.stereotype.Component;

@Component
public class NewMapFactory {

    public Map create(String name, PositiveInt columns, PositiveInt rows) {
        return new Map(name, columns, rows);
    }
}
