package com.github.davinkevin.poc.podcastserver.repository;

import com.github.davinkevin.poc.podcastserver.entity.Item;
import io.vavr.collection.Set;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

/**
 * Created by kevin on 14/07/2017
 */
@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final DSLContext dsl;
}
