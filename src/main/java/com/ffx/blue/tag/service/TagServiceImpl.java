package com.ffx.blue.tag.service;

import com.ffx.blue.article.service.ArticleService;
import com.ffx.blue.tag.converter.TagConverter;
import com.ffx.blue.tag.domain.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

import static com.ffx.blue.common.utils.ClassUtils.ARTICLES_COLLECTION_NAME;

/**
 * Service class providing operations for {@link Tag} resource.
 *
 * @see ArticleService
 * @see TagConverter
 */
@Service
@CacheConfig(cacheNames = ARTICLES_COLLECTION_NAME)
public class TagServiceImpl implements TagService {

    private ArticleService articleService;

    private TagConverter tagConverter;

    @Autowired
    public TagServiceImpl(ArticleService articleService, TagConverter tagConverter) {
        this.articleService = articleService;
        this.tagConverter = tagConverter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable
    public Optional<Tag> findByDateAndTagName(Date date, String tagName) {
        return Optional.ofNullable(tagConverter
                .withTagName(tagName)
                .convert(articleService.findByDateAndTagNames(date, tagName)));
    }
}
