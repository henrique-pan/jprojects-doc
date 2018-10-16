package com.grasset.main;

import com.grasset.interceptor.LogIdInterceptor;
import com.grasset.utils.ExternalConfig;
import com.grasset.utils.PathHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;

@EnableWebMvc
@Configuration
@EnableDiscoveryClient
public class ApplicationConfig implements WebMvcConfigurer {

    @Autowired
    private ExternalConfig externalConfig;

    @Autowired
    private LogIdInterceptor logIdInterceptor;

    @PostConstruct
    private void setExternalConfig() {
        PathHelper.setRootPath(externalConfig.rootPathURL);
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logIdInterceptor);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowCredentials(true);
    }

}
