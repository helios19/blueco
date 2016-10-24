package com.ffx.blue.article.handler;

import com.ffx.blue.article.exception.ArticleNotFoundException;
import com.ffx.blue.article.exception.InvalidArticleException;
import com.ffx.blue.article.exception.InvalidParameterException;
import com.ffx.blue.common.handler.GlobalExceptionHandler;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception handler class used for catching any {@link com.ffx.blue.article.domain.Article} related exceptions
 * and transforming them into HATEOAS JSON message.
 *
 * @see VndErrors
 * @see com.ffx.blue.article.domain.Article
 * @see InvalidArticleException
 * @see InvalidParameterException
 * @see ArticleNotFoundException
 */
@ControllerAdvice
public class ArticleExceptionHandler extends GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(InvalidArticleException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    VndErrors handleInvalidArticleException(InvalidArticleException ex) {
        return getVndErrors(ex);
    }

    @ResponseBody
    @ExceptionHandler(InvalidParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    VndErrors handleInvalidParameterException(InvalidParameterException ex) {
        return getVndErrors(ex);
    }

    @ResponseBody
    @ExceptionHandler(ArticleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    VndErrors handleArticleNotFoundException(ArticleNotFoundException ex) {
        return getVndErrors(ex);
    }

}
