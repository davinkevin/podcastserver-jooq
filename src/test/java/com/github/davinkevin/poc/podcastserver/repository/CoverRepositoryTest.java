package com.github.davinkevin.poc.podcastserver.repository;

import com.github.davinkevin.poc.podcastserver.config.JooqConfig;
import com.github.davinkevin.poc.podcastserver.entity.Cover;
import io.vavr.control.Option;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase;


/**
 * Created by kevin on 14/07/2017
 */
@JdbcTest
@SpringBootTest(classes = {CoverRepository.class, DataSourceAutoConfiguration.class, JooqAutoConfiguration.class, JooqConfig.class})
@RunWith(SpringRunner.class)
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:database/schema-h2.sql", "classpath:database/data-h2.sql"})
public class CoverRepositoryTest {

    private @Autowired CoverRepository coverRepository;

    @Test
    public void should_find_one() {
        /* GIVEN */
        /* WHEN  */
        Option<Cover> cover = coverRepository.findOne(UUID.fromString("e858960d-5e97-41e6-a6be-0449084729c3"));
        /* THEN  */
        assertThat(cover.isEmpty()).isFalse();
        assertThat(cover.get().getHeight()).isEqualTo(1400);
        assertThat(cover.get().getWidth()).isEqualTo(1400);
        assertThat(cover.get().getUrl()).isEqualTo("/api/podcasts/dd8241c7-4da9-4225-93c5-a9dd4c49a471/cover.jpg");
    }

    @Test
    public void should_create() {
        /* GIVEN */
        Cover cover = Cover.builder()
                    .height(400)
                    .width(400)
                    .url("http://foo.bar.com/url.png")
                .build();

        /* WHEN  */
        Cover savedCover = coverRepository.create(cover);

        /* THEN  */
        assertThat(savedCover).isNotNull();
        assertThat(savedCover.getUrl()).isEqualTo(cover.getUrl());
        assertThat(savedCover.getWidth()).isEqualTo(cover.getWidth());
        assertThat(savedCover.getHeight()).isEqualTo(cover.getHeight());
    }

    @Test
    public void should_update() {
        /* GIVEN */
        Cover cover = Cover.builder()
                .id(UUID.fromString("e858960d-5e97-41e6-a6be-0449084729c3"))
                .height(400)
                .width(400)
                .url("http://foo.bar.com/url.png")
                .build();

        /* WHEN  */
        Cover savedCover = coverRepository.update(cover);

        /* THEN  */
        assertThat(savedCover).isNotNull();
        assertThat(savedCover.getUrl()).isEqualTo(cover.getUrl());
        assertThat(savedCover.getWidth()).isEqualTo(cover.getWidth());
        assertThat(savedCover.getHeight()).isEqualTo(cover.getHeight());
    }

    @Test
    public void should_delete_by_id() {
        /* GIVEN */
        UUID id = UUID.fromString("2517a82e-846d-4593-ba6b-b65b0c515811");

        /* WHEN  */
        coverRepository.delete(id);
        Option<Cover> cover = coverRepository.findOne(id);

        /* THEN  */
        assertThat(cover).isEmpty();
    }


}