package manfred.manfreditor.application;

import manfred.data.DataContext;
import manfred.data.infrastructure.map.TileConversionRule;
import manfred.manfreditor.application.startup.LoadKnownMapsCommand;
import manfred.manfreditor.common.CommonContext;
import manfred.manfreditor.common.command.Command;
import manfred.manfreditor.application.startup.LoadKnownMapObjectsCommand;
import manfred.manfreditor.map.MapContext;
import manfred.manfreditor.map.view.mapobject.MapObjectsView;
import manfred.manfreditor.map.view.mapobject.ObjectsViewCoordinateFactory;
import manfred.manfreditor.map.model.accessibility.AccessibilityMerger;
import manfred.manfreditor.map.model.Map;
import manfred.manfreditor.map.model.MapModel;
import manfred.manfreditor.map.model.ObjectInsertionValidator;
import manfred.manfreditor.map.model.objectfactory.ConcreteMapObjectFactory;
import manfred.manfreditor.map.model.mapobject.MapObject;
import manfred.manfreditor.map.model.mapobject.SelectedObject;
import manfred.manfreditor.map.model.mapobject.SelectionState;
import org.eclipse.swt.graphics.ImageLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.HashMap;
import java.util.List;

@Configuration
@ComponentScan(basePackages = "manfred.manfreditor.application")
@Import({DataContext.class, CommonContext.class, MapContext.class})
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
    public List<Command> startupCommands(
        LoadKnownMapObjectsCommand loadKnownMapObjectsCommand,
        LoadKnownMapsCommand loadKnownMapsCommand
    ) {
        return List.of(loadKnownMapObjectsCommand, loadKnownMapsCommand);
    }

    @Bean
    public ImageLoader swtImageLoader() {
        return new ImageLoader();
    }
}
