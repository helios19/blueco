package com.ffx.blue;

import com.ffx.blue.article.domain.Article;
import com.ffx.blue.article.repository.ArticleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.ffx.blue.common.utils.ClassUtils.toDate;

/**
 * Main Spring Boot Application class. Note that a {@link CommandLineRunner} is created
 * to initialize the Mongo database with a set of articles.
 */
@EnableMongoRepositories(basePackages = "com.ffx.blue.article.repository")
@SpringBootApplication
public class Application {

    public static final void main(String... args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Initializes the Mongodb database with a set of articles (tagged with news, health and sport).
     *
     * @param articleRepository Article repository to save the articles
     * @return CommandLineRunner
     */
    @Bean
    CommandLineRunner init(ArticleRepository articleRepository) {

        return (evt) -> articleRepository.saveOrUpdate(
                Arrays.asList("news,health,sport".split(","))
                        .stream()
                        .map(s -> Article
                                .newInstance()
                                .withTitle("title of the " + s + " article")
                                .withBody("body of the " + s + " article")
                                .withTags(Arrays.asList(s, "science", "fitness"))
                                .withDate(toDate("2016-09-22"))
                        )
                        .collect(Collectors.toList()).toArray(new Article[]{})
        );
    }

}
