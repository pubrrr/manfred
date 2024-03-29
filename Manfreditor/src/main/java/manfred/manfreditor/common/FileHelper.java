package manfred.manfreditor.common;

import io.vavr.control.Try;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class FileHelper {

    public Try<Path> write(File file, String content) {
        return Try.of(() -> Files.write(file.toPath(), content.getBytes()));
    }

    public File[] getFilesIn(File directory) {
        return directory.listFiles();
    }
}
