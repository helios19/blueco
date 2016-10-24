package com.ffx.blue.common.sequence;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static com.ffx.blue.common.utils.ClassUtils.COUNTERS_COLLECTION_NAME;

/**
 * Counter document class holding up the sequence value of a given collection.
 *
 * @see com.ffx.blue.common.service.CounterServiceImpl
 */
@Document(collection = COUNTERS_COLLECTION_NAME)
public class Counter {
    @Id
    private String id;
    @Field
    private int seq;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public Counter withId(String id) {
        this.id = id; return this;
    }

    public Counter withSeq(int seq) {
        this.seq = seq; return this;
    }

}