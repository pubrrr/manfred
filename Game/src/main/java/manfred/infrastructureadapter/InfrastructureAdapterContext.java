package manfred.infrastructureadapter;

import manfred.game.config.GameConfig;
import manfred.infrastructureadapter.map.tile.TileConversionRule;
import manfred.infrastructureadapter.person.gelaber.GelaberConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static manfred.infrastructureadapter.map.tile.TileConversionRule.createAccessible;
import static manfred.infrastructureadapter.map.tile.TileConversionRule.createDoor;
import static manfred.infrastructureadapter.map.tile.TileConversionRule.createNonAccessible;
import static manfred.infrastructureadapter.map.tile.TileConversionRule.createPerson;
import static manfred.infrastructureadapter.map.tile.TileConversionRule.createPortal;
import static manfred.infrastructureadapter.map.tile.TileConversionRule.decorateWithImage;

@Configuration
@ComponentScan(basePackages = "manfred.infrastructureadapter")
public class InfrastructureAdapterContext {

    @Bean
    public TileConversionRule tileConversionRule(GameConfig gameConfig, GelaberConverter gelaberConverter) {
        return createPerson(gameConfig, gelaberConverter)
            .orElse(createPortal())
            .orElse(createDoor())
            .orElse(decorateWithImage(gameConfig).and(createAccessible().orElse(createNonAccessible())))
            .orElse(createAccessible())
            .orElse(createNonAccessible());
    }
}
