package com.github.davinkevin.poc.podcastserver.config;

import com.github.davinkevin.poc.podcastserver.repository.listener.UUIDListener;
import org.jooq.RecordListenerProvider;
import org.jooq.impl.DefaultRecordListenerProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by kevin on 15/07/2017
 */
@Configuration
public class JooqConfig {

    @Bean
    public RecordListenerProvider[] providers() {
        return DefaultRecordListenerProvider.providers(new UUIDListener());
    }

}
