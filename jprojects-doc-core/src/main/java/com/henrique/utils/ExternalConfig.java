package com.henrique.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@PropertySource("file:./config/constants.properties")
public class ExternalConfig {

    @Value("${root.path.url}")
    public String rootPathURL;

}
