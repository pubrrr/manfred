package manfred.manfreditor;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class ManfreditorApplication {
    private ConfigurableApplicationContext applicationContext;

    public void init() {
        this.applicationContext = new SpringApplicationBuilder()
            .sources(Manfreditor.class)
            .run();
    }

    public void stop() {
        this.applicationContext.close();
    }
}
