package com.github.davinkevin.poc.podcastserver.repository;

import com.github.davinkevin.poc.podcastserver.entity.Podcast;
import com.github.davinkevin.poc.podcastserver.entity.Tag;
import io.vavr.collection.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static io.vavr.API.Set;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by kevin on 14/07/2017
 */
@JdbcTest
@SpringBootTest(classes = {PodcastRepository.class, DataSourceAutoConfiguration.class, JooqAutoConfiguration.class})
@RunWith(SpringRunner.class)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:database/schema-h2.sql", "classpath:database/data-h2.sql"})
public class PodcastRepositoryTest {

    private @Autowired PodcastRepository podcastRepository;

    @Test
    public void should_find_by_tag() {
        /* GIVEN */
        Tag tag = Tag.builder()
                .id(UUID.fromString("c557656c-289d-41b2-8b28-39ed769958ae"))
                .name("Humour")
                .build();

        /* WHEN  */
        Set<Podcast> podcasts = podcastRepository.findByTag(Set(tag));

        /* THEN */
        assertThat(podcasts).isNotEmpty();
    }

}