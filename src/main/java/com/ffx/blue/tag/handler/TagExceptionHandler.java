package com.ffx.blue.tag.handler;

import com.ffx.blue.common.handler.AbstractExceptionHandler;
import com.ffx.blue.tag.exception.TagNotFoundException;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception handler class used for catching any {@link com.ffx.blue.tag.domain.Tag} related exceptions
 * and transforming them into HATEOAS JSON message.
 *
 * @see com.ffx.blue.tag.domain.Tag
 * @see TagNotFoundException
 */
@ControllerAdvice
public class TagExceptionHandler extends AbstractExceptionHandler {

    @ResponseBody
    @ExceptionHandler(TagNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    VndErrors handleTagNotFoundException(TagNotFoundException ex) {
        return getVndErrors(ex);
    }

}
