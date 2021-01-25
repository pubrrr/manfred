package manfred.data.persistence.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RawMapTileDto {
    private String name;
    private List<String> structure;
    @JsonIgnore
    private BufferedImage image;
}
