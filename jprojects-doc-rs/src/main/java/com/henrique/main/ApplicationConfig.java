package com.henrique.main;

import com.henrique.interceptor.LogIdInterceptor;
import com.henrique.utils.ExternalConfig;
import com.henrique.utils.PathHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;

@EnableWebMvc
@Configuration
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

}
