package manfred.game.conversion.map;

import manfred.data.persistence.dto.MapObjectDto;

import java.util.List;
import java.util.Optional;

public abstract class SpecialTileFactory<T extends MapObjectDto> {

    protected Optional<T> findDtoAt(List<T> dtos, int x, int y) {
        return dtos.stream()
            .filter(dto -> dto.getPositionX() == x && dto.getPositionY() == y)
            .findFirst();
    }
}
