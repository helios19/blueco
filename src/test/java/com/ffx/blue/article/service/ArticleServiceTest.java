package com.ffx.blue.article.service;

import com.ffx.blue.article.domain.Article;
import com.ffx.blue.article.repository.ArticleRepository;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.SpringApplicationConfiguration;
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

@ActiveProfiles({"test", "cacheDisabled"})
@SpringApplicationConfiguration(classes = ArticleServiceTest.TestAppConfig.class,
        initializers = ConfigFileApplicationContextInitializer.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ArticleServiceTest {

    @InjectMocks
    private ArticleServiceImpl articleService;

    @Mock
    private ArticleRepository repository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    private Article sampleArticle = Article.newInstance()
            .withId("1")
            .withVersion(1l)
            .withBody("article body")
            .withDate(toDate("2016-10-01"))
            .withTags(Arrays.asList("news", "sport"))
            .withTitle("article title");

    @Test
    @DirtiesContext
    public void shouldFindArticleById() {
        // given
        when(repository.findById(any(String.class))).thenReturn(
                Optional.of(sampleArticle));

        // when
        Optional<Article> article = articleService.findById("1");

        // then
        assertTrue(article.isPresent());
        assertNotNull(article.get());
        assertEquals(article.get().getId(), "1");
        assertEquals(article.get().getVersion(), 1l, 0);
        assertEquals(article.get().getTitle(), "article title");
        assertEquals(article.get().getBody(), "article body");
        assertEquals(article.get().getTags(), Arrays.asList("news", "sport"));
        assertEquals(article.get().getDate(), toDate("2016-10-01"));
        verify(repository, times(1)).findById(any(String.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DirtiesContext
    public void shouldFindAll() {
        // given
        when(repository.findAll()).thenReturn(
                Lists.newArrayList(sampleArticle));

        // when
        List<Article> articles = articleService.findAll();

        // then
        assertFalse(articles.isEmpty());
        assertTrue(articles.size() == 1);
        assertNotNull(articles.get(0));
        assertEquals(articles.get(0).getId(), "1");
        assertEquals(articles.get(0).getVersion(), 1l, 0);
        assertEquals(articles.get(0).getTitle(), "article title");
        assertEquals(articles.get(0).getBody(), "article body");
        assertEquals(articles.get(0).getTags(), Arrays.asList("news", "sport"));
        assertEquals(articles.get(0).getDate(), toDate("2016-10-01"));
        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DirtiesContext
    public void shouldSaveArticle() {
        // when
        articleService.save(sampleArticle);

        // then
        verify(repository, times(1)).saveOrUpdate(sampleArticle);
        verifyNoMoreInteractions(repository);
    }

    @Configuration
//    @EnableAutoConfiguration
//    @EnableMongoRepositories(basePackages = "com.ffx.blue.article.repository")
    @ComponentScan({
            "com.ffx.blue.article.service"
    })
    public static class TestAppConfig {

        @Bean
        public ArticleRepository articleRepository() {
            return null;
        }
    }
}

