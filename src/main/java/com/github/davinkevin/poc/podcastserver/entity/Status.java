package com.github.davinkevin.poc.podcastserver.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.vavr.collection.Set;
import io.vavr.control.Option;

import static io.vavr.API.Set;

public enum Status {
    NOT_DOWNLOADED,
    DELETED,
    STARTED,
    FINISH,
    STOPPED,
    PAUSED;

    /**
     * Created by kevin on 18/02/15.
     */
    private static final Set<Status> _values = Set(Status.values());

    @JsonCreator
    public static Status of(String v) {
        return Status.from(v).getOrElseThrow(() -> new IllegalArgumentException("No enum constant Status." + v));
    }

    public static Option<Status> from(String v) {
        return _values.find(s -> s.toString().equals(v));
    }
    
}
