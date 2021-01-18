package backend.trade.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @program: trade
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-05-18 14:02
 **/



@Configuration
@EnableConfigurationProperties(AppProperties.class)
@PropertySource("classpath:app.properties")
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String appId;
    private String appSecret;


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}

