package com.ffx.blue.tag.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when no {@link com.ffx.blue.tag.domain.Tag} resource cannot be found.
 *
 * @see com.ffx.blue.tag.domain.Tag
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TagNotFoundException extends RuntimeException {
    public TagNotFoundException(String tagName) {
        super("Tag not found with tagName:" + tagName);
    }
}
