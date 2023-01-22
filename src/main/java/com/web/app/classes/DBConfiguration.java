package com.web.app.classes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class DBConfiguration {

    @Bean(name="Database")
    public Database getDatabase(){
        return new Database();
    }
}
