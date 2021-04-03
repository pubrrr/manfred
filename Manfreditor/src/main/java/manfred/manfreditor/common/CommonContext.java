package manfred.manfreditor.common;

import manfred.data.DataContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "manfred.manfreditor.common")
@Import({DataContext.class})
public class CommonContext {
}
