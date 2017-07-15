package com.github.davinkevin.poc.podcastserver.entity;

import com.fasterxml.jackson.annotation.JsonView;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.UUID;

import static io.vavr.API.Set;
/**
 * Created by kevin on 17/01/2016 for PodcastServer
 */
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class WatchList {

//    @Id
//    @GeneratedValue
//    @Column(columnDefinition = "UUID")
    private UUID id;
    private String name;

//    @ManyToMany
    @JsonView(WatchListDetailsListView.class)
    private Set<Item> items = HashSet.empty();

    public interface WatchListDetailsListView {}
}
