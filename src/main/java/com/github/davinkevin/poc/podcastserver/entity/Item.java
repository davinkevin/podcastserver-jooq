package com.github.davinkevin.poc.podcastserver.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

import static io.vavr.API.Option;
import static io.vavr.API.Try;
import static java.util.Objects.nonNull;

@Slf4j
@Builder
@Getter @Setter
@Accessors(chain = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@NoArgsConstructor @AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true, value = { "numberOfTry", "localUri", "addATry", "deleteDownloadedFile", "localPath", "proxyURLWithoutExtention", "extention", "hasValidURL", "reset", "coverPath" })
public class Item {

    public  static Path rootFolder;
    public  static final Item DEFAULT_ITEM = new Item();
    private static final String PROXY_URL = "/api/podcasts/%s/items/%s/download%s";
    private static final String COVER_PROXY_URL = "/api/podcasts/%s/items/%s/cover.%s";

    private UUID id;

    private Cover cover;

    @JsonBackReference("podcast-item")
    private Podcast podcast;

    @NotNull
    @JsonView(ItemSearchListView.class)
    private String title;

    @JsonView(ItemSearchListView.class)
    private String url;

    @JsonView(ItemPodcastListView.class)
    private ZonedDateTime pubDate;

    @JsonView(ItemPodcastListView.class)
    private String description;

    @JsonView(ItemSearchListView.class)
    private String mimeType;

    @JsonView(ItemDetailsView.class)
    private Long length;

    @JsonView(ItemDetailsView.class)
    private String fileName;

    /* Value for the Download */
    @JsonView(ItemSearchListView.class)
    private Status status = Status.NOT_DOWNLOADED;

    @JsonView(ItemDetailsView.class)
    private Integer progression = 0;

    private Integer numberOfTry = 0;

    @JsonView(ItemDetailsView.class)
    private ZonedDateTime downloadDate;

    private ZonedDateTime creationDate;

    @JsonIgnore
    private java.util.Set<WatchList> watchLists = new HashSet<>();


    public String getLocalUri() {
        return (fileName == null) ? null : getLocalPath().toString();
    }

    public Item setLocalUri(String localUri) {
        fileName = FilenameUtils.getName(localUri);
        return this;
    }

    public Item addATry() {
        this.numberOfTry++;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        if (this == DEFAULT_ITEM && o != DEFAULT_ITEM || this != DEFAULT_ITEM && o == DEFAULT_ITEM) return false;

        Item item = Item.class.cast(o);

        if (nonNull(id) && nonNull(item.id))
            return id.equals(item.id);

        if (nonNull(url) && nonNull(item.url)) {
            return url.equals(item.url);
        }

        return StringUtils.equals(getProxyURL(), item.getProxyURL());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(url)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", pubDate=" + pubDate +
                ", description='" + description + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", length=" + length +
                ", status='" + status + '\'' +
                ", progression=" + progression +
                ", downloaddate=" + downloadDate +
                ", podcast=" + podcast +
                ", numberOfTry=" + numberOfTry +
                '}';
    }

    @JsonProperty("proxyURL") @JsonView(ItemSearchListView.class)
    public String getProxyURL() {
        return String.format(PROXY_URL, podcast.getId(), id, getExtension());
    }

    @JsonProperty("isDownloaded") @JsonView(ItemSearchListView.class)
    public Boolean isDownloaded() {
        return StringUtils.isNotEmpty(fileName);
    }
    
    //* CallBack Method JPA *//
    //@PreRemove
    public void preRemove() {
        checkAndDelete();
        //watchLists.forEach(watchList -> watchList.remove(this));
    }

    private void checkAndDelete() {

        if (!podcast.getHasToBeDeleted()) {
            return;
        }

        Try(() -> Files.deleteIfExists(getCoverPath()))
            .onFailure(e -> log.error("Error during deletion of cover of {}", this, e));

        if (isDownloaded()) {
            deleteFile();
        }
    }

    private void deleteFile() {
        Try(() -> Files.deleteIfExists(getLocalPath())).onFailure(e -> log.error("Error during deletion of {}", this, e));
    }

    @JsonIgnore
    public Item deleteDownloadedFile() {
        deleteFile();
        status = Status.DELETED;
        fileName = null;
        return this;
    }

    public Path getLocalPath() {
        return getPodcastPath().resolve(fileName);
    }

    public Path getCoverPath() {
        return Option(cover)
                .map(Cover::getUrl)
                .orElse(() -> Option(""))
                .map(FilenameUtils::getExtension)
                .map(ext -> getPodcastPath().resolve(id + "." + ext))
                .getOrElseThrow(() -> new RuntimeException("Cover Path not found for item " + title));
    }

    private Path getPodcastPath() {
        return rootFolder.resolve(podcast.getTitle());
    }

    public String getProxyURLWithoutExtention() {
        return String.format(PROXY_URL, podcast.getId(), id, "");
    }

    private String getExtension() {
        return Option(fileName)
                .map(FilenameUtils::getExtension)
                .map(ext -> "."+ext)
                .getOrElse("");
    }

    @JsonProperty("cover") @JsonView(ItemSearchListView.class)
    public Cover getCoverOfItemOrPodcast() {
        return Option(cover)
                .map(c -> String.format(COVER_PROXY_URL, podcast.getId(), id, FilenameUtils.getExtension(c.getUrl())))
                .map(url -> cover.toBuilder().url(url).build())
                .getOrElse(() -> podcast.getCover());
    }

    @JsonProperty("podcastId") @JsonView(ItemSearchListView.class)
    public UUID getPodcastId() { return Option(podcast).map(Podcast::getId).getOrElse(() -> null);}
        
    @AssertTrue
    public boolean hasValidURL() {
        return (!StringUtils.isEmpty(this.url)) || Objects.equals("upload", podcast.getType());
    }

    public Item reset() {
        checkAndDelete();
        setStatus(Status.NOT_DOWNLOADED);
        downloadDate = null;
        fileName = null;
        return this;
    }

    public interface ItemSearchListView {}
    public interface ItemPodcastListView extends ItemSearchListView {}
    public interface ItemDetailsView extends ItemPodcastListView {}
}
