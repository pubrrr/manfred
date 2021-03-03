package manfred.data.persistence.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import manfred.data.InvalidInputException;
import manfred.data.persistence.ObjectReader;
import manfred.data.persistence.dto.AttackDto;
import manfred.data.shared.PositiveInt;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class AttackReader implements ObjectReader<AttackSource, AttackDto> {

    private final ObjectMapper objectMapper;
    private final ImageLoader imageLoader;

    public AttackReader(ObjectMapper objectMapper, ImageLoader imageLoader) {
        this.objectMapper = objectMapper;
        this.imageLoader = imageLoader;
    }

    public AttackDto load(String name) throws InvalidInputException {
        URL yamlURL = getClass().getResource("/attacks/" + name + ".yaml");
        if (yamlURL == null) {
            throw new InvalidInputException("Did not find resource for attack " + name);
        }

        URL imageDirectoryUrl;
        try {
            imageDirectoryUrl = new File(yamlURL.getFile()).getParentFile().toURI().toURL();
        } catch (MalformedURLException e) {
            throw new InvalidInputException("Could not convert to attack image directory url: " + e.getMessage());
        }

        return load(yamlURL, imageDirectoryUrl);
    }

    @Override
    public AttackDto load(AttackSource source) throws InvalidInputException {
        return load(source.getAttackUrl(), source.getImageDirectoryUrl());
    }

    AttackDto load(URL yamlURL, URL imageDirectoryUrl) throws InvalidInputException {
        try {
            AttackDto attackDto = objectMapper.readValue(yamlURL, AttackDto.class);
            attackDto.setAttackAnimation(loadAttackAnimation(imageDirectoryUrl, attackDto.getName(), attackDto.getNumberOfAnimationImages()));
            return attackDto;
        } catch (IOException e) {
            throw new InvalidInputException("Could not read attack from " + yamlURL.toString(), e);
        }
    }

    private List<BufferedImage> loadAttackAnimation(URL imageDirectoryUrl, String name, PositiveInt numberOfAnimationImages) throws InvalidInputException {
        List<BufferedImage> attackAnimation = new ArrayList<>(numberOfAnimationImages.value());
        for (int idx = 0; idx < numberOfAnimationImages.value(); idx++) {
            File file = new File(imageDirectoryUrl.getFile(), name + "_" + idx + ".png");
            try {
                attackAnimation.add(imageLoader.load(file.toURI().toURL()));
            } catch (MalformedURLException e) {
                throw new InvalidInputException("Could not convert file " + file.getAbsolutePath() + " to URL for attack " + name, e);
            }
        }
        return Collections.unmodifiableList(attackAnimation);
    }
}
