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
import static manfred.infrastructureadapter.map.tile.TileConversionRule.decorateWithSprite;
import static manfred.infrastructureadapter.map.tile.TileConversionRule.wrapForGraphicsDebugging;

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
