package manfred.data.infrastructure.person;

import lombok.AllArgsConstructor;
import manfred.data.InvalidInputException;
import manfred.data.persistence.dto.MapPersonDto;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@AllArgsConstructor
public class PersonsLoader {

    private final PersonProvider personProvider;

    public List<PersonPrototype> load(List<MapPersonDto> personsOnMap) throws InvalidInputException {
        List<String> errorMessages = new LinkedList<>();
        List<PersonPrototype> result = personsOnMap.stream().map(mapPersonDto -> {
            try {
                return personProvider.provide(mapPersonDto.getName()).at(mapPersonDto.getPositionX(), mapPersonDto.getPositionY());
            } catch (InvalidInputException e) {
                errorMessages.add(e.getMessage());
                return null;
            }
        }).collect(toList());

        if (!errorMessages.isEmpty()) {
            throw new InvalidInputException(String.join(",\n", errorMessages));
        }
        return result;
    }
}
