package com.ffx.blue.article.repository;

import com.ffx.blue.article.domain.Article;
import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodExecutable;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.ffx.blue.common.utils.ClassUtils.toDate;
import static org.junit.Assert.*;

@ActiveProfiles({"test", "cacheDisabled"})
@SpringApplicationConfiguration(classes = ArticleRepositoryTest.TestAppConfig.class,
        initializers = ConfigFileApplicationContextInitializer.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ArticleRepositoryTest {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ArticleRepository repository;

    @Autowired
    private MongodExecutable mongodExec;

    @Autowired
    private MongoClient mongoClient;

    private static MongodExecutable staticMongodExec;
    private static MongoClient staticMongoClient;


    private Article sampleArticle = Article.newInstance()
            .withId("1")
            .withVersion(1l)
            .withBody("article body")
            .withDate(toDate("2016-10-01"))
            .withTags(Arrays.asList("news", "sport"))
            .withTitle("article title");


    @Before
    public void setUp() throws Exception {

        // init mongodb
        staticMongodExec = mongodExec;
        staticMongoClient = mongoClient;
    }


    @After
    public void tearDown() throws Exception {
        mongoTemplate.dropCollection(Article.class);
    }

    @AfterClass
    public static void postContruct() {
        // stop mongodb
        staticMongoClient.close();
        staticMongodExec.stop();
    }

    @Test
    public void shouldFindArticleById() throws Exception {
        // given
        mongoTemplate.insert(sampleArticle);

        // when
        Optional<Article> article = repository.findById("1");

        // then
        assertTrue(article.isPresent());
        assertNotNull(article.get());
        assertEquals("1", article.get().getId());
        assertEquals(0l, article.get().getVersion(), 0);
        assertEquals("article title", article.get().getTitle());
        assertEquals("article body", article.get().getBody());
        assertEquals(Arrays.asList("news", "sport"), article.get().getTags());
        assertEquals(toDate("2016-10-01"), article.get().getDate());

    }

    @Test
    public void shouldFindArticleByTagNames() throws Exception {
        // given
        mongoTemplate.insert(sampleArticle);

        // when
        List<Article> articles = repository.findByTagNames("news", "unrelated tag");

        // then
        assertFalse(articles.isEmpty());
        assertTrue(articles.size() == 1);
        assertNotNull(articles.get(0));
        assertEquals("1", articles.get(0).getId());
        assertEquals(0l, articles.get(0).getVersion(), 0);
        assertEquals("article title", articles.get(0).getTitle());
        assertEquals("article body", articles.get(0).getBody());
        assertEquals(Arrays.asList("news", "sport"), articles.get(0).getTags());
        assertEquals(toDate("2016-10-01"), articles.get(0).getDate());
    }

    @Test
    public void shouldNotFindArticleByTagNames() throws Exception {
        // given
        mongoTemplate.insert(sampleArticle);

        // when
        List<Article> articles = repository.findByTagNames("unrelated tag", "another unrelated tag");

        // then
        assertTrue(articles.isEmpty());
    }

    @Test
    public void shouldSaveOrUpdateArticle() throws Exception {
        // given
        List<Article> articlesBefore = mongoTemplate.findAll(Article.class);

        // when
        repository.saveOrUpdate(sampleArticle);

        // then
        List<Article> articlesAfter = mongoTemplate.findAll(Article.class);

        assertTrue(articlesBefore.isEmpty());
        assertFalse(articlesAfter.isEmpty());
        assertTrue(articlesAfter.size() == 1);
        assertNotNull(articlesAfter.get(0));
        assertEquals("1", articlesAfter.get(0).getId());
        assertEquals(1l, articlesAfter.get(0).getVersion(), 0);
        assertEquals("article title", articlesAfter.get(0).getTitle());
        assertEquals("article body", articlesAfter.get(0).getBody());
        assertEquals(Arrays.asList("news", "sport"), articlesAfter.get(0).getTags());
        assertEquals(toDate("2016-10-01"), articlesAfter.get(0).getDate());
    }


    @Configuration
    @EnableAutoConfiguration
    @EnableMongoRepositories(basePackages = "com.ffx.blue.article.repository")
    @ComponentScan({
            "com.ffx.blue.article.service",
            "com.ffx.blue.tag.service",
            "com.ffx.blue.common.service"
    })
    public static class TestAppConfig {
    }

}