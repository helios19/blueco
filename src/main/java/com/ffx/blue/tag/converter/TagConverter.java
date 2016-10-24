package com.ffx.blue.tag.converter;


import com.ffx.blue.article.domain.Article;
import com.ffx.blue.common.converter.Converter;
import com.ffx.blue.tag.domain.Tag;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Converter class that maps a {@link List<Article>} to an {@link Tag} instance.
 *
 * @see Converter
 * @see Article
 * @see Tag
 */
@Component
public class TagConverter implements Converter<List<Article>, Tag> {

    private String tagName;

    /**
     * Converts {@link List<Article>} to an {@link Tag} instance. This method requires
     * the {@code source} and {@code tagName} arguments not to be null, otherwise throws
     * an {@code NullPointerException}.
     *
     * @param source the source object to convert, which must be an instance of {@code S} (never {@code null})
     * @return tag instance converted or NullPointerException if either {@code source} or {@code tagName} is null.
     * @throws IllegalArgumentException if the list of articles cannot be converted.
     */
    @Override
    public Tag convert(List<Article> source) {
        Objects.requireNonNull(source, "source must not be null.");
        Objects.requireNonNull(tagName, "tagName must not be null.");

        if (CollectionUtils.isEmpty(source)) {
            throw new IllegalArgumentException("empty list of articles.");
        }

        return Tag.newInstance()
                .withArticles(
                        source
                                .stream()
                                .map(article -> article.getId())
                                .collect(Collectors.toList()))

                .withCount(source.size())
                .withRelatedTags(source.stream()
                        .flatMap(article -> article.getTags().stream().filter(s -> !StringUtils.equals(s, tagName)))
                        .distinct().collect(Collectors.toSet()))
                .withTag(tagName);
    }

    public TagConverter withTagName(String tagName) {
        this.tagName = tagName; return this;
    }

}
