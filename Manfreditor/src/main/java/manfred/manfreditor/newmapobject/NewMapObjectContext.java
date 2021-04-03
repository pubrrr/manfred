package manfred.manfreditor.newmapobject;

import manfred.data.DataContext;
import manfred.manfreditor.common.CommonContext;
import org.eclipse.swt.graphics.ImageLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "manfred.manfreditor.newmapobject")
@Import({DataContext.class, CommonContext.class})
public class NewMapObjectContext {

    @Bean
    public ImageLoader swtImageLoader() {
        return new ImageLoader();
    }
}
