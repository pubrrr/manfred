package manfred.data.persistence.dto;

import manfred.data.shared.PositiveInt;

public interface MapObjectDto {

    PositiveInt getPositionX();
    PositiveInt getPositionY();
    String getTargetToLoad();
}
