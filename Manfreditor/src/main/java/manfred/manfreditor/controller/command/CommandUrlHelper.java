package manfred.manfreditor.controller.command;

import org.springframework.stereotype.Component;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

@Component
public class CommandUrlHelper {

    public URL getUrlForFile(String filePath) throws MalformedURLException {
        return new File(filePath).toURI().toURL();
    }
}
