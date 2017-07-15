package com.github.davinkevin.poc.podcastserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.jackson.datatype.VavrModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by kevin on 15/06/2016 for Podcast Server
 */
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper mapper() {
        return new ObjectMapper().registerModules(new VavrModule());
    }

}
