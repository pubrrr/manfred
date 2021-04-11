package manfred.manfreditor.map;

import manfred.data.DataContext;
import manfred.data.infrastructure.map.TileConversionRule;
import manfred.data.persistence.reader.MapSource;
import manfred.manfreditor.map.model.Map;
import manfred.manfreditor.map.model.MapModel;
import manfred.manfreditor.map.model.ObjectInsertionValidator;
import manfred.manfreditor.map.model.accessibility.AccessibilityMerger;
import manfred.manfreditor.map.model.mapobject.MapObject;
import manfred.manfreditor.map.model.mapobject.SelectedObject;
import manfred.manfreditor.map.model.mapobject.SelectionState;
import manfred.manfreditor.map.model.objectfactory.ConcreteMapObjectFactory;
import manfred.manfreditor.map.view.mapobject.MapObjectsView;
import manfred.manfreditor.map.view.mapobject.ObjectsViewCoordinateFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.io.File;
import java.util.HashMap;

@Configuration
@ComponentScan(basePackages = "manfred.manfreditor.map")
@Import({DataContext.class})
public class MapContext {

    @Bean
    public MapModel mapModel(AccessibilityMerger accessibilityMerger, ObjectInsertionValidator objectInsertionValidator) {
        File mapFile = new File("uninitializedMap");
        mapFile.setReadOnly();
        return new MapModel(new Map("uninitialized", new HashMap<>(), new MapSource(mapFile)), accessibilityMerger, objectInsertionValidator);
    }

    @Bean
    public TileConversionRule<MapObject> tileConversionRule(ConcreteMapObjectFactory concreteMapObjectFactory) {
        return concreteMapObjectFactory;
    }

    @Bean
    public ObjectsViewCoordinateFactory objectsViewCoordinateFactory() {
        return new ObjectsViewCoordinateFactory(MapObjectsView.NUMBER_OF_COLUMNS);
    }

    @Bean
    public SelectedObject selectedObject() {
        return new SelectedObject(SelectionState.empty());
    }
}
