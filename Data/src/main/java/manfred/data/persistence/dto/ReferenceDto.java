package manfred.data.persistence.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReferenceDto {
    private String to;
    private String text = "";
    private boolean continueTalking = false;
}
