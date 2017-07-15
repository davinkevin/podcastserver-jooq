package com.github.davinkevin.poc.podcastserver.repository.listener;

import com.github.davinkevin.poc.podcastserver_jooq.h2.tables.records.CoverRecord;
import org.jooq.RecordContext;
import org.jooq.impl.DefaultRecordListener;

import java.util.UUID;

/**
 * Created by kevin on 15/07/2017
 */
public class UUIDListener extends DefaultRecordListener {

    @Override
    public void insertStart(RecordContext ctx) {
        // Generate an ID for inserted CoverRecord
        if (ctx.record() instanceof CoverRecord) {
            CoverRecord book = (CoverRecord) ctx.record();
            book.setId(UUID.randomUUID());
        }
    }
}
