package tr.com.cemalaydin.dashboard;

import tr.com.cemalaydin.dashboard.config.AppConfig;
import tr.com.cemalaydin.dashboard.helpers.JedisHelper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class StaticContextInitializer {

    @Autowired
    private AppConfig appConfig;
    @Autowired
    private ApplicationContext context;
    @PostConstruct
    public void init() {
        JedisHelper.setAppConfig(appConfig);
    }
}
