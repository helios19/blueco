package com.ffx.blue.article.repository;

import com.ffx.blue.article.domain.Article;
import com.ffx.blue.common.service.CounterService;
import com.google.common.base.Strings;
import com.mongodb.MongoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Repository implementation class methods to manipulate {@link Article} resource in database.
 * This class inherits from {@link ArticleRepositoryCustom}
 *
 * @see ArticleRepositoryCustom
 */
@Repository
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

    private static final Logger LOG = LoggerFactory.getLogger(ArticleRepositoryImpl.class);

    private MongoTemplate mongoTemplate;

    private CounterService counterService;

    @Autowired
    public ArticleRepositoryImpl(MongoTemplate mongoTemplate, CounterService counterService) {
        this.mongoTemplate = mongoTemplate;
        this.counterService = counterService;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Article> findByTagNames(String... tagNames) {

        Criteria criteria = Criteria.where("tags").in((Object[]) tagNames);

        return mongoTemplate.find(Query.query(criteria), Article.class);
    }

    @Override
    public List<Article> findByDateAndTagNames(Date date, String... tagNames) {

        Criteria criteria =
                Criteria.where("tags").in((Object[]) tagNames)
                        .andOperator(
                                Criteria.where("date").is(date));

        return mongoTemplate.find(Query.query(criteria), Article.class);

    }

    @Override
    public void saveOrUpdate(Article... articles) {

        Arrays.asList(articles).stream().forEach(article -> {
            Criteria criteria = getCriteria(article);

            Update update = getUpdate(article);

            Query query = new Query(criteria);

            try {
                mongoTemplate.upsert(query, update, Article.class);
            } catch (MongoException me) {
                LOG.error("An error occurred while upserting article[{},{}] ",
                        article.getTitle(), article.getDate(), me);
            }
        });

    }

    private Update getUpdate(Article article) {
        return new Update()
                .set("id", new Integer(counterService.getNextSequence("articles")).toString())
                .set("body", article.getBody())
                .set("tags", article.getTags())
                .set("title", article.getTitle())
                .set("date", article.getDate());
    }

    private Criteria getCriteria(Article article) {

        Criteria criteria;

        if (!Strings.isNullOrEmpty(article.getId())) {
            criteria = Criteria.where("id").is(article.getId());
        } else {
            criteria = new Criteria()
                    .andOperator(
                            Criteria.where("title").is(article.getTitle()),
                            Criteria.where("date").is(article.getDate()));
        }

        return criteria;
    }

    @Override
    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    @Override
    public void setMongoTemplate(MongoTemplate template) {
        this.mongoTemplate = template;
    }

}