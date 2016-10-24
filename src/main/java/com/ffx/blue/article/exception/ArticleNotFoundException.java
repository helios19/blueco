package com.ffx.blue.article.exception;

import com.ffx.blue.article.domain.Article;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when no {@link Article} resource cannot be found.
 *
 * @see Article
 * @see HttpStatus
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ArticleNotFoundException extends RuntimeException {
    public ArticleNotFoundException(String articleId) {
        super("Article not found with id:" + articleId);
    }
}
