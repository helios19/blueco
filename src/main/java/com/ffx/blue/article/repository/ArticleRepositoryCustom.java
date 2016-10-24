package com.ffx.blue.article.repository;

import com.ffx.blue.article.domain.Article;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ArticleRepositoryCustom {

    void saveOrUpdate(Article... article);

    List<Article> findByTagNames(String... tagNames);

    List<Article> findByDateAndTagNames(Date date, String... tagNames);

    MongoTemplate getMongoTemplate();

    void setMongoTemplate(MongoTemplate template);

}
