package com.my.myapiclientsdk;

import com.my.myapiclientsdk.client.NameApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author:crj
 * @Desc:
 * @create: 2024-05-04
 **/
@Configuration
@ConfigurationProperties("myapi.client")
@Data
@ComponentScan
public class MyApiClientConfig {
    private String accessKey;
    private String secretKey;
    @Bean
    public NameApiClient nameApiClient(){
        return new NameApiClient(accessKey,secretKey);
    }
}
