package com.github.davinkevin.poc.podcastserver.repository.converter;

import org.jooq.Converter;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Created by kevin on 14/07/2017
 */
public class ZonedDateTimeConverter implements Converter<Timestamp, ZonedDateTime> {

    private static final long serialVersionUID = 1L;

    @Override
    public ZonedDateTime from(final Timestamp timestamp) {
        return ZonedDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault());
    }

    @Override
    public Timestamp to(final ZonedDateTime zonedDateTime) {
        return Timestamp.from(zonedDateTime.toInstant());
    }

    @Override
    public Class<Timestamp> fromType() {
        return Timestamp.class;
    }

    @Override
    public Class<ZonedDateTime> toType() {
        return ZonedDateTime.class;
    }
}
