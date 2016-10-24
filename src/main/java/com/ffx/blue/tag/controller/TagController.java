package com.ffx.blue.tag.controller;

import com.ffx.blue.tag.domain.Tag;
import com.ffx.blue.tag.exception.TagNotFoundException;
import com.ffx.blue.tag.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static com.ffx.blue.common.utils.ClassUtils.DATE_FORMAT_PATTERN;
import static com.ffx.blue.common.utils.ClassUtils.DEFAULT_PAGE_SIZE;

/**
 * Tag controller class defining the HTTP operations available for the {@link Tag} resource.
 *
 * @see Tag
 * @see RestController
 */
@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Returns a JSON {@link Tag} object given the {@code name} and {@code date} parameters.
     *
     * @param tagName Article tag name
     * @param date Article date
     * @param pageable Pageable instance defining size, page or sort parameters
     * @return JSON Tag object
     * @throws TagNotFoundException if no Article could match the input parameters
     */
    @RequestMapping(value = "/{tagName}/{date}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Tag> findByTagNameAndDate(
            @PathVariable("tagName") String tagName,
            @PathVariable("date") @DateTimeFormat(pattern = DATE_FORMAT_PATTERN) Date date,
            @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable) {

        Tag tag = tagService.findByDateAndTagName(date, tagName)
                .orElseThrow(() -> new TagNotFoundException(tagName));

        return ResponseEntity
                .ok()
                .body(tag);
    }

}
