package com.github.davinkevin.poc.podcastserver.repository;

import com.github.davinkevin.poc.podcastserver.entity.Cover;
import com.github.davinkevin.poc.podcastserver.entity.Podcast;
import com.github.davinkevin.poc.podcastserver.entity.Tag;
import com.github.davinkevin.poc.podcastserver.repository.converter.ZonedDateTimeConverter;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.impl.SQLDataType;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.UUID;

import static com.github.davinkevin.poc.podcastserver_jooq.h2.Tables.*;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;

/**
 * Created by kevin on 14/07/2017
 */
@Repository
@RequiredArgsConstructor
public class PodcastRepository {

    private static final DataType<ZonedDateTime> ZONED_DATE_TIME = SQLDataType.TIMESTAMP.asConvertedDataType(new ZonedDateTimeConverter());
    private static final Field<ZonedDateTime> LAST_UPDATE = field(name(PODCAST.LAST_UPDATE.getName()), ZONED_DATE_TIME);


    private final DSLContext dsl;

    public Set<Podcast> findByTag(Set<Tag> tags) {

        Set<UUID> ids = tags.map(Tag::getId);

        // @formatter:off
        return dsl
            .select(
                PODCAST.ID, PODCAST.DESCRIPTION, PODCAST.HAS_TO_BE_DELETED, PODCAST.SIGNATURE, PODCAST.TITLE, PODCAST.TYPE, PODCAST.URL, LAST_UPDATE,
                COVER.ID, COVER.URL, COVER.WIDTH, COVER.HEIGHT,
                TAG.ID, TAG.NAME
            ).from(PODCAST)
                .innerJoin(PODCAST_TAGS)
                    .on(PODCAST_TAGS.PODCASTS_ID.eq(PODCAST.ID))
                .innerJoin(TAG)
                    .on(PODCAST_TAGS.TAGS_ID.eq(TAG.ID))
                .innerJoin(COVER)
                    .on(PODCAST.COVER_ID.eq(COVER.ID))
            .where(TAG.ID.in(ids.toJavaList()))
            .fetchStream()
            .map(v -> buildPodcast(v)
                    .cover(CoverRepository.toCover(v))
                    .build())
            .collect(HashSet.collector());
        // @formatter:on
    }

    public static Podcast.PodcastBuilder buildPodcast(Record r) {
        return Podcast.builder()
                    .id(r.get(PODCAST.ID))
                    .title(r.get(PODCAST.TITLE))
                    .description(r.get(PODCAST.DESCRIPTION))
                    .hasToBeDeleted(r.get(PODCAST.HAS_TO_BE_DELETED))
                    .signature(r.get(PODCAST.SIGNATURE))
                    .type(r.get(PODCAST.TYPE))
                    .url(r.get(PODCAST.URL))
                    .lastUpdate(r.get(LAST_UPDATE));
    }

}
