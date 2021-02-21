package manfred.infrastructureadapter;

import manfred.game.config.GameConfig;
import manfred.data.infrastructure.map.TileConversionRule;
import manfred.infrastructureadapter.person.gelaber.GelaberConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static manfred.infrastructureadapter.map.tile.GameTileFactoryRules.createAccessible;
import static manfred.infrastructureadapter.map.tile.GameTileFactoryRules.createDoor;
import static manfred.infrastructureadapter.map.tile.GameTileFactoryRules.createNonAccessible;
import static manfred.infrastructureadapter.map.tile.GameTileFactoryRules.createPerson;
import static manfred.infrastructureadapter.map.tile.GameTileFactoryRules.createPortal;
import static manfred.infrastructureadapter.map.tile.GameTileFactoryRules.decorateWithSprite;
import static manfred.infrastructureadapter.map.tile.GameTileFactoryRules.wrapForGraphicsDebugging;

@Configuration
@ComponentScan(basePackages = "manfred.infrastructureadapter")
public class InfrastructureAdapterContext {

    @Bean
    public TileConversionRule tileConversionRule(GameConfig gameConfig, GelaberConverter gelaberConverter) {
        TileConversionRule tileConversionRule = createPerson(gameConfig, gelaberConverter)
            .orElse(createPortal())
            .orElse(createDoor())
            .orElse(decorateWithSprite(gameConfig).and(createAccessible().orElse(createNonAccessible())))
            .orElse(createAccessible())
            .orElse(createNonAccessible());
        if (gameConfig.isDebugGraphics()) {
            return wrapForGraphicsDebugging(tileConversionRule, gameConfig);
        }
        return tileConversionRule;
    }
}
