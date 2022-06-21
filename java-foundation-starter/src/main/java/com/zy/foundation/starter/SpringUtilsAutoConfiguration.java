package com.zy.foundation.starter;

import com.zy.foundation.lang.utils.SpringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringUtilsAutoConfiguration {
    @Bean
    public SpringUtils springUtils() {
        return new SpringUtils();
    }
}
