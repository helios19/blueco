package com.ffx.blue.tag.service;

import com.ffx.blue.tag.domain.Tag;

import java.util.Date;
import java.util.Optional;


/**
 * Service interface providing method declarations for CRUD operations for the {@link Tag} resource.
 *
 * @see Tag
 * @see TagServiceImpl
 */
public interface TagService {

    /**
     * Returns an {@link Optional<Tag>} instance given a {@code tagName} and {@code date} arguments.
     *
     * @param tagName Article's tag name
     * @param date    Article's date
     * @return Optional tag instance
     */
    Optional<Tag> findByDateAndTagName(Date date, String tagName);
}
