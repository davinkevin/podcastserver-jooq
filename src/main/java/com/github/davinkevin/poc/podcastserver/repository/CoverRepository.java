package com.github.davinkevin.poc.podcastserver.repository;

import com.github.davinkevin.poc.podcastserver.entity.Cover;
import com.github.davinkevin.poc.podcastserver.entity.Tag;
import com.github.davinkevin.poc.podcastserver_jooq.h2.Tables;
import com.github.davinkevin.poc.podcastserver_jooq.h2.tables.records.CoverRecord;
import io.vavr.API;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record12;
import org.jooq.Result;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.UUID;

import static com.github.davinkevin.poc.podcastserver_jooq.h2.Tables.*;
import static io.vavr.API.*;
import static io.vavr.API.Option;

/**
 * Created by kevin on 14/07/2017.
 */
@Repository
@RequiredArgsConstructor
public class CoverRepository {

    private final DSLContext dsl;

    public Option<Cover> findOne(UUID id) {
        return Option(dsl.selectFrom(COVER)
                .where(COVER.ID.eq(id))
                .fetchOneInto(Cover.class));
    }

    public Cover create(Cover cover) {
        CoverRecord record = dsl.newRecord(COVER, cover);

        record.store();

        return record.into(Cover.class);
    }

    public Cover update(Cover cover) {
        CoverRecord record = dsl.newRecord(COVER, cover);

        record.update();

        return record.into(Cover.class);
    }

    public void delete(UUID id) {
        CoverRecord record = dsl.newRecord(COVER);

        record.setId(id);
        record.delete();
    }

    public static Cover toCover(Record v) {
        return Cover.builder()
                    .id(v.get(COVER.ID))
                    .url(v.get(COVER.URL))
                    .width(v.get(COVER.WIDTH))
                    .height(v.get(COVER.HEIGHT))
                .build();
    }
}
