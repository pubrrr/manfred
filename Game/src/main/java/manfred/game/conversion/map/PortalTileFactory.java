package manfred.game.conversion.map;

import manfred.data.persistence.dto.TransporterDto;
import manfred.data.infrastructure.map.ValidatedMapDto;
import manfred.game.interact.Portal;
import manfred.game.map.MapTile;

import java.util.Optional;
import java.util.function.Function;

public class PortalTileFactory extends SpecialTileFactory<TransporterDto> implements TileConversionRule {

    @Override
    public Optional<TileConversionAction> applicableTo(ValidatedMapDto input, int x, int y) {
        return findDtoAt(input.getPortals(), x, y)
            .map(portalDto -> new TileFromDtoAction<>(portalDto, portalFactory()));
    }

    private Function<TransporterDto, MapTile> portalFactory() {
        return portalDto -> new Portal(portalDto.getTarget(), portalDto.getTargetSpawnX(), portalDto.getTargetSpawnY());
    }
}
