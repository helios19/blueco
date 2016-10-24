package com.ffx.blue.article.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

import static com.ffx.blue.common.utils.ClassUtils.DATE_FORMAT_PATTERN;

/**
 * Plain java class representing an article resource. This class is annotated with either
 * Mongodb and Jackson tags as the same class declaration will be used to save in database
 * and serialize to JSON.
 *
 * <p>This class also declares a compound-index based on {@code title} and {@code date} fields.</p>
 */
@Document
@CompoundIndexes({
        @CompoundIndex(name = "article_title_date_idx", def = "{'title': 1, 'date': 1}")
})
public class Article {
    @Id
    private String id;

    @Field
    @Version
    private Long version;

    @Field
    @Indexed
    @NotNull
    private String title;

    @Field
    @Indexed
    @NotNull
    @JsonFormat(pattern = DATE_FORMAT_PATTERN)
    private Date date;

    @Field
    @NotNull
    private String body;

    @Field
    @NotNull
    private List<String> tags = Lists.newArrayList();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Article withId(String id) {
        this.id = id; return this;
    }

    public Article withVersion(Long version) {
        this.version = version; return this;
    }

    public Article withTags(List<String> tags) {
        this.tags = tags; return this;
    }

    public Article withTitle(String title) {
        this.title = title; return this;
    }

    public Article withBody(String body) {
        this.body = body; return this;
    }

    public Article withDate(Date date) {
        this.date = date; return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Article article = (Article) o;

        if (id != null ? !id.equals(article.id) : article.id != null) return false;
        if (version != null ? !version.equals(article.version) : article.version != null) return false;
        if (title != null ? !title.equals(article.title) : article.title != null) return false;
        if (date != null ? !date.equals(article.date) : article.date != null) return false;
        if (body != null ? !body.equals(article.body) : article.body != null) return false;
        return tags != null ? tags.equals(article.tags) : article.tags == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("version", version)
                .add("title", title)
                .add("date", date)
                .add("body", body)
                .add("tags", tags)
                .toString();
    }

    /**
     * Creates a new instance of the class represented by this Article object.
     *
     * @return Article instance
     */
    public static Article newInstance() {
        return new Article();
    }

}
