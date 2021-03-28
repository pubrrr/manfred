package manfred.manfreditor;

import manfred.data.DataContext;
import manfred.data.infrastructure.map.TileConversionRule;
import manfred.manfreditor.controller.command.Command;
import manfred.manfreditor.controller.command.startup.LoadKnownMapObjectsCommand;
import manfred.manfreditor.gui.view.mapobject.MapObjectsView;
import manfred.manfreditor.gui.view.mapobject.ObjectsViewCoordinateFactory;
import manfred.manfreditor.map.accessibility.AccessibilityMerger;
import manfred.manfreditor.map.Map;
import manfred.manfreditor.map.MapModel;
import manfred.manfreditor.map.ObjectInsertionValidator;
import manfred.manfreditor.map.objectfactory.ConcreteMapObjectFactory;
import manfred.manfreditor.mapobject.MapObject;
import manfred.manfreditor.mapobject.SelectedObject;
import manfred.manfreditor.mapobject.SelectionState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.HashMap;
import java.util.List;

@Configuration
@ComponentScan(basePackages = "manfred.manfreditor")
@Import({DataContext.class})
public class ManfreditorContext {

    @Bean
    public MapModel mapModel(AccessibilityMerger accessibilityMerger, ObjectInsertionValidator objectInsertionValidator) {
        return new MapModel(new Map("uninitialized", new HashMap<>()), accessibilityMerger, objectInsertionValidator);
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

    @Bean("StartupCommands")
    public List<Command> startupCommands(LoadKnownMapObjectsCommand loadKnownMapObjectsCommand) {
        return List.of(loadKnownMapObjectsCommand);
    }
}
