package com.ffx.blue.article.service;

import com.ffx.blue.article.domain.Article;
import com.ffx.blue.article.repository.ArticleRepository;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service interface providing method declarations for CRUD operations for the {@link Article} resource.
 *
 * @see Article
 * @see ArticleServiceImpl
 */
public interface ArticleService {
    /**
     * Saves an {@link Optional<Article>} instance.
     *
     * @param article Article object to save
     */
    void save(Article article);

    /**
     * Returns an {@link Optional<Article>} instance given {@code id} argument.
     *
     * @param id Article's identifier
     * @return Optional article
     */
    Optional<Article> findById(String id);

    /**
     * Return a list of {@link Article} given {@code date} and {@code tagNames} arguments.
     *
     * @param date     Article's date
     * @param tagNames Article's tag names
     * @return List of articles found
     */
    List<Article> findByDateAndTagNames(Date date, String... tagNames);

    /**
     * Returns a list of all {@link Article}.
     *
     * @return List of articles found
     */
    List<Article> findAll();

    /**
     * Return a list of {@link Article} given {@link Pageable} argument.
     *
     * @param pageable Pageable argument
     * @return List of articles found
     */
    List<Article> findAll(Pageable pageable);

    /**
     * Sets a {@link ArticleRepository} instance.
     *
     * @param repository Article repository instance
     */
    void setRepository(ArticleRepository repository);
}
