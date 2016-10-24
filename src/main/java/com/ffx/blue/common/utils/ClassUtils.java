package com.ffx.blue.common.utils;

import com.ffx.blue.article.domain.Article;
import com.ffx.blue.article.exception.InvalidArticleException;
import com.ffx.blue.article.exception.InvalidParameterException;
import com.google.common.primitives.Ints;
import org.apache.commons.collections.CollectionUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * Utils class providing convenient factory and helper methods for {@link Article} and {@link com.ffx.blue.tag.domain.Tag} resources.
 */
public class ClassUtils {
    public static final String COUNTERS_COLLECTION_NAME = "counters";
    public static final String ARTICLES_COLLECTION_NAME = "articles";
    public static final String DATE_FORMAT_PATTERN = "yyyyMMdd";
    public static final DateTimeFormatter FORMATTER = ofPattern("yyyy-MM-dd");
    public static final int DEFAULT_PAGE_SIZE = 50;

    private ClassUtils() {
    }

    /**
     * Validates article fields.
     *
     * @param article  Article instance to validate
     * @throws InvalidArticleException in case one of the article fields is invalid
     */
    public static void validateArticle(Article article) {

        // validate article

        if(article == null || CollectionUtils.isEmpty(article.getTags())) {
            throw new InvalidArticleException(article);
        }

    }

    /**
     * Validates article identifier field.
     *
     * @param id Article identifier
     * @return Article identifier if valid
     * @throws InvalidParameterException in case article identifier is invalid
     */
    public static String validateArticleId(String id) {
        if(Ints.tryParse(id) == null) {
            throw new InvalidParameterException("id", id);
        }

        return id;
    }

    /**
     * Converts {@code isoDate} argument to {@link Date}.
     *
     * @param isoDate character sequence to convert
     * @return Date instance
     */
    public static Date toDate(String isoDate) {
        return Date.from(
                Instant.from(
                        LocalDate.parse(isoDate, FORMATTER).atStartOfDay(ZoneId.systemDefault())));
    }

}
