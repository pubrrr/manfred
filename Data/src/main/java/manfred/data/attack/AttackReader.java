package manfred.data.attack;

import com.fasterxml.jackson.databind.ObjectMapper;
import manfred.data.InvalidInputException;
import manfred.data.ObjectReader;
import manfred.data.image.ImageLoader;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class AttackReader implements ObjectReader<AttackDto> {

    private final ObjectMapper objectMapper;
    private final ImageLoader imageLoader;

    public AttackReader(ObjectMapper objectMapper, ImageLoader imageLoader) {
        this.objectMapper = objectMapper;
        this.imageLoader = imageLoader;
    }

    public AttackDto load(String name) throws InvalidInputException {
        URL yamlURL = getClass().getResource("/attack/" + name + ".yaml");
        if (yamlURL == null) {
            throw new InvalidInputException("Did not find resource for attack " + name);
        }

        return load(name, yamlURL);
    }

    AttackDto load(String name, URL yamlURL) throws InvalidInputException {
        try {
            AttackDto attackDto = objectMapper.readValue(yamlURL, AttackDto.class);
            attackDto.setAttackAnimation(loadAttackAnimation(name, attackDto.getNumberOfAnimationImages()));
            return attackDto;
        } catch (IOException e) {
            throw new InvalidInputException("Could not read attack " + name, e);
        }
    }

    private List<BufferedImage> loadAttackAnimation(String name, int numberOfAnimationImages) throws InvalidInputException {
        List<BufferedImage> attackAnimation = new ArrayList<>(numberOfAnimationImages);
        for (int idx = 0; idx < numberOfAnimationImages; idx++) {
            attackAnimation.add(
                imageLoader.load(getClass().getResource("/attack/" + name + "_" + idx + ".yaml"))
            );
        }
        return Collections.unmodifiableList(attackAnimation);
    }
}
