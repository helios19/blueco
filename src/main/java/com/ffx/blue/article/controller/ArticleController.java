package com.ffx.blue.article.controller;

import com.ffx.blue.article.domain.Article;
import com.ffx.blue.article.exception.ArticleNotFoundException;
import com.ffx.blue.article.exception.InvalidArticleException;
import com.ffx.blue.article.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collection;

import static com.ffx.blue.common.utils.ClassUtils.*;

/**
 * Article controller class defining the HTTP operations available for the {@link Article} resource.
 *
 * @see Article
 * @see RestController
 */
@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * Create an article given the {@code article} request object.
     *
     * <p>Note that this method only produces and consumes JSON UTF-8 objects. Any parameters
     * receives will be validated before being processed.</p>
     *
     * @param article Article request object to create
     * @return Http Header values (e.g uri of new created article)
     * @throws InvalidArticleException if object request contains incorrect values
     */
    @RequestMapping(method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> addArticle(@Validated @RequestBody Article article) {

        validateArticle(article);

        articleService.save(article);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(article.getId()).toUri());

        return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
    }

    /**
     * Returns a collection of articles stored in the system.
     *
     * <p>The number of articles returned is currently limited by {@link com.ffx.blue.common.utils.ClassUtils#DEFAULT_PAGE_SIZE}.
     * To increase this number a {@code size} request parameter can be provided to the uri.<p/>
     *
     * <p>This method also supports pagination through the {@code page} request parameter.</p>
     *
     * @param pageable Pageable instance defining size, page or sort parameters
     * @return Collection of articles
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Collection<Article> findAll(@PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        return articleService.findAll(pageable);
    }

    /**
     * Returns an article given the {@code id} parameter representing its identifier.
     *
     * @param id Article identifier
     * @return article found
     * @throws ArticleNotFoundException if no article for the given identifier can be found
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Article> findById(@PathVariable("id") String id) {

        Article article = articleService.findById(
                validateArticleId(id))
                .orElseThrow(() -> new ArticleNotFoundException(id));

        return ResponseEntity
                .ok()
                .eTag(article.getVersion().toString())
                .body(article);
    }

}
