package com.ffx.blue.article.exception;

import com.ffx.blue.article.domain.Article;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an {@link com.ffx.blue.article.domain.Article} instance contains invalid field values
 * (e.g {@link com.ffx.blue.article.domain.Article#tags} list is empty, invalid fields body, etc.)
 *
 * @see com.ffx.blue.article.domain.Article
 * @see HttpStatus
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidArticleException extends RuntimeException {
    public InvalidArticleException(Article article) {
        super("Invalid Article field values [" + article + "]");
    }
}
