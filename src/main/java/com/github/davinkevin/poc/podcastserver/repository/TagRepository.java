package com.github.davinkevin.poc.podcastserver.repository;

import com.github.davinkevin.poc.podcastserver.entity.Tag;
import io.vavr.API;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static com.github.davinkevin.poc.podcastserver_jooq.h2.Tables.COVER;
import static com.github.davinkevin.poc.podcastserver_jooq.h2.Tables.TAG;

/**
 * Created by kevin on 14/07/2017
 */
@Repository
@RequiredArgsConstructor
public class TagRepository {

    private final DSLContext dsl;

    public Option<Tag> findOne(UUID id) {
        return API.Option(dsl.selectFrom(TAG)
                .where(TAG.ID.eq(id))
                .fetchOneInto(Tag.class));
    }

    public Tag create(Tag tag) {
        UUID id = UUID.randomUUID();
        dsl.insertInto(TAG)
                .set(TAG.ID, id)
                .set(TAG.NAME, tag.getName())
                .execute();

        return findOne(id).getOrElseThrow(() -> new RuntimeException("Tag not found after creation"));
    }

    public Tag update(Tag tag) {
        dsl.update(TAG)
                .set(TAG.ID, tag.getId())
                .set(TAG.NAME, tag.getName())
                .where(TAG.ID.eq(tag.getId()))
                .execute();

        return findOne(tag.getId()).getOrElseThrow(() -> new RuntimeException("Tag not found after update"));
    }
}
