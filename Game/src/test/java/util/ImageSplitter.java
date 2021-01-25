package util;

import manfred.data.InvalidInputException;
import manfred.data.persistence.reader.ImageLoader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageSplitter {
    public static void main(String[] args) throws IOException, InvalidInputException {
        ImageLoader imageLoader = new ImageLoader();
        BufferedImage image = imageLoader.load("data\\manfred\\manfred.png");

        int horizontalSplits = 8;
        int verticalSplits = 4;

        String[] outputNameY = new String[]{"down", "left", "right", "up"};

        int subimageWidth = image.getWidth() / horizontalSplits;
        int subimageHeight = image.getHeight() / verticalSplits;
        for (int idx_X = 0; idx_X < horizontalSplits; idx_X++) {
            for (int idx_Y = 0; idx_Y < verticalSplits; idx_Y++) {
                BufferedImage subimage = image.getSubimage(idx_X * subimageWidth, idx_Y * subimageHeight, subimageWidth, subimageHeight);
                File output = new File("data\\manfred\\manfred_" + outputNameY[idx_Y] + idx_X + ".png");
                ImageIO.write(subimage, "png", output);
            }
        }
    }
}
