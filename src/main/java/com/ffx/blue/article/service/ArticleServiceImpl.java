package com.ffx.blue.article.service;

import com.ffx.blue.article.domain.Article;
import com.ffx.blue.article.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.ffx.blue.common.utils.ClassUtils.ARTICLES_COLLECTION_NAME;

/**
 * Service class providing CRUD operations for {@link Article} resource.
 *
 * @see ArticleRepository
 */
@Service
@CacheConfig(cacheNames = ARTICLES_COLLECTION_NAME)
public class ArticleServiceImpl implements ArticleService {

    private ArticleRepository repository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(allEntries = true)
    public void save(Article article) {
        repository.saveOrUpdate(article);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable
    public Optional<Article> findById(String id) {
        return repository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable
    public List<Article> findByDateAndTagNames(Date date, String... tagNames) {
        return repository.findByDateAndTagNames(date, tagNames);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable
    public List<Article> findAll() {
        return repository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable
    public List<Article> findAll(Pageable pageable) {
        Page<Article> articles = repository.findAll(pageable);
        return articles.getContent();
    }

    /**
     * Sets an {@link ArticleRepository} instance.
     *
     * @param repository Article repository instance
     */
    public void setRepository(ArticleRepository repository) {
        this.repository = repository;
    }
}
