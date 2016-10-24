package com.ffx.blue.tag.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

/**
 * Plain java class representing an Tag resource. Note that this class is expected to produce
 * a {@code related_tags} field when converted into JSON.
 */
public class Tag {
    private String tag;

    private Integer count;

    private List<String> articles = Lists.newArrayList();

    @JsonProperty("related_tags")
    private Set<String> relatedTags = Sets.newHashSet();

    public static Tag newInstance() {
        return new Tag();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<String> getArticles() {
        return articles;
    }

    public void setArticles(List<String> articles) {
        this.articles = articles;
    }

    public Set<String> getRelatedTags() {
        return relatedTags;
    }

    public void setRelatedTags(Set<String> relatedTags) {
        this.relatedTags = relatedTags;
    }

    public Tag withTag(String tag) {
        this.tag = tag; return this;
    }

    public Tag withCount(Integer count) {
        this.count = count; return this;
    }

    public Tag withArticles(List<String> articles) {
        this.articles = articles; return this;
    }

    public Tag withRelatedTags(Set<String> relatedTags) {
        this.relatedTags = relatedTags; return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag1 = (Tag) o;

        if (tag != null ? !tag.equals(tag1.tag) : tag1.tag != null) return false;
        if (count != null ? !count.equals(tag1.count) : tag1.count != null) return false;
        if (articles != null ? !articles.equals(tag1.articles) : tag1.articles != null) return false;
        return relatedTags != null ? relatedTags.equals(tag1.relatedTags) : tag1.relatedTags == null;

    }

    @Override
    public int hashCode() {
        int result = tag != null ? tag.hashCode() : 0;
        result = 31 * result + (count != null ? count.hashCode() : 0);
        result = 31 * result + (articles != null ? articles.hashCode() : 0);
        result = 31 * result + (relatedTags != null ? relatedTags.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("tag", tag)
                .add("count", count)
                .add("articles", articles)
                .add("relatedTags", relatedTags)
                .toString();
    }

}
