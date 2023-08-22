package tr.com.cemalaydin.dashboard.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
@Data
public class AppConfig {
   private Boolean securityEnabled;
   private String redisServerHost;
   private String redisServerPassword;
}
