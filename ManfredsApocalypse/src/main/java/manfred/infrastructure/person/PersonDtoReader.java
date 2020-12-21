package manfred.infrastructure.person;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class PersonDtoReader {
    private final ObjectMapper objectMapper;

    public PersonDtoReader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public PersonDto read(String value) throws JsonProcessingException {
        return objectMapper.readValue(value, PersonDto.class);
    }
}
