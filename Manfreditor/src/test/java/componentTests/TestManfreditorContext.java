package componentTests;

import manfred.manfreditor.application.ManfreditorContext;
import manfred.manfreditor.common.FileHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
public class TestManfreditorContext extends ManfreditorContext {

    @Bean
    public FileHelper fileHelper() {
        return mock(FileHelper.class);
    }
}
