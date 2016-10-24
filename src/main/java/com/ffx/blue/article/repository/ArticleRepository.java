package com.ffx.blue.article.repository;

import com.ffx.blue.article.domain.Article;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends MongoRepository<Article, String>, ArticleRepositoryCustom {

    Optional<Article> findById(String id);

}

