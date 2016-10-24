package com.ffx.blue.cache;

import com.ffx.blue.article.domain.Article;
import com.ffx.blue.article.repository.ArticleRepository;
import com.ffx.blue.article.service.ArticleService;
import com.ffx.blue.common.utils.ClassUtils;
import com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.ffx.blue.common.utils.ClassUtils.toDate;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles({"test"})
@SpringApplicationConfiguration(classes = ArticleCacheIT.TestCacheConfig.class,
        initializers = ConfigFileApplicationContextInitializer.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ArticleCacheIT {

    @Autowired
    @InjectMocks
    private ArticleService articleService;

    @Mock
    private ArticleRepository repository;

    @Autowired
    private CacheManager cacheManager;

    private Article sampleArticle = Article.newInstance()
            .withId("1")
            .withVersion(1l)
            .withBody("article body")
            .withDate(toDate("2016-10-01"))
            .withTags(Arrays.asList("news", "sport"))
            .withTitle("article title");

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        articleService.save(sampleArticle);
        articleService.setRepository(repository);
    }

    @After
    public void tearDown() throws Exception {
        // reset article cache
        cacheManager.getCache(ClassUtils.ARTICLES_COLLECTION_NAME).clear();
    }

    @Test
    @DirtiesContext
    public void shouldFindArticleByIdFromCache() {

        // given
        when(repository.findById(any(String.class)))
                .thenReturn(Optional.of(sampleArticle));

        String id = "1";
        Cache articleCache = cacheManager.getCache(ClassUtils.ARTICLES_COLLECTION_NAME);
        Cache.ValueWrapper beforeFillingCache = articleCache.get(id);


        // when
        Optional<Article> articleFromRepo = articleService.findById(id);
        Optional<Article> articleFromCache = articleService.findById(id);

        // then
        Cache.ValueWrapper afterFillingCache = articleCache.get(id);
        assertNull(beforeFillingCache);
        assertNotNull(afterFillingCache);
        assertTrue(articleFromRepo.isPresent());
        assertTrue(articleFromCache.isPresent());
        assertEquals(articleFromCache, articleFromRepo);

        verify(repository, times(1)).findById(any(String.class));
    }

    @Test
    @DirtiesContext
    public void shouldFindAllArticlesFromCache() {

        // given
        when(repository.findAll()).thenReturn(
                Lists.newArrayList(sampleArticle));

        // when
        List<Article> articlesFromRepo = articleService.findAll();
        List<Article> articlesFromCache = articleService.findAll();

        // then
        assertFalse(articlesFromRepo.isEmpty());
        assertFalse(articlesFromCache.isEmpty());
        assertEquals(articlesFromCache, articlesFromRepo);

        verify(repository, times(1)).findAll();

    }

    @Configuration
    @ComponentScan({"com.ffx.blue.article.service", "com.ffx.blue.article.domain"})
    @EnableAutoConfiguration
    @EnableCaching
    public static class TestCacheConfig {
        @Bean
        CacheManager cacheManager() {
            return new ConcurrentMapCacheManager(ClassUtils.ARTICLES_COLLECTION_NAME);
        }
    }

}
