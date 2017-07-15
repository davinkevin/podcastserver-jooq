package com.github.davinkevin.poc.podcastserver.repository;

import com.github.davinkevin.poc.podcastserver.entity.Tag;
import com.github.davinkevin.poc.podcastserver.entity.WatchList;
import io.vavr.API;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static com.github.davinkevin.poc.podcastserver_jooq.h2.Tables.COVER;
import static com.github.davinkevin.poc.podcastserver_jooq.h2.Tables.TAG;
import static com.github.davinkevin.poc.podcastserver_jooq.h2.Tables.WATCH_LIST;

/**
 * Created by kevin on 14/07/2017
 */
@Repository
@RequiredArgsConstructor
public class WatchListRepository {

    private final DSLContext dsl;

    public Option<WatchList> findOne(UUID id) {
        return API.Option(dsl.selectFrom(WATCH_LIST)
                .where(WATCH_LIST.ID.eq(id))
                .fetchOneInto(WatchList.class));
    }

    public WatchList create(Tag tag) {
        UUID id = UUID.randomUUID();
        dsl.insertInto(WATCH_LIST)
                .set(WATCH_LIST.ID, id)
                .set(WATCH_LIST.NAME, tag.getName())
                .execute();

        return findOne(id).getOrElseThrow(() -> new RuntimeException("WatchList not found after creation"));
    }

    public WatchList update(Tag tag) {
        dsl.update(WATCH_LIST)
                .set(WATCH_LIST.ID, tag.getId())
                .set(WATCH_LIST.NAME, tag.getName())
                .where(COVER.ID.eq(tag.getId()))
                .execute();

        return findOne(tag.getId()).getOrElseThrow(() -> new RuntimeException("WatchList not found after update"));
    }



}
