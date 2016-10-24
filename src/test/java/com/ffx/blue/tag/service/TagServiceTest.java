package com.ffx.blue.tag.service;

import com.ffx.blue.article.domain.Article;
import com.ffx.blue.article.repository.ArticleRepository;
import com.ffx.blue.article.service.ArticleService;
import com.ffx.blue.tag.domain.Tag;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static com.ffx.blue.common.utils.ClassUtils.toDate;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles({"test"})
@SpringApplicationConfiguration(classes = {TagServiceTest.TestAppConfig.class},
        initializers = ConfigFileApplicationContextInitializer.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TagServiceTest {

    @Mock
    private ArticleService articleService;

    @Autowired
    @InjectMocks
    private TagServiceImpl tagService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private Article sampleArticle = Article.newInstance()
            .withId("1")
            .withVersion(1l)
            .withBody("article body")
            .withDate(toDate("2016-10-01"))
            .withTags(Arrays.asList("news", "health"))
            .withTitle("article title");

    @Test
    @DirtiesContext
    public void shouldFindTagByArticleDateAndNames() {
        // given
        when(articleService.findByDateAndTagNames(any(Date.class), any(String.class))).thenReturn(
                Lists.newArrayList(sampleArticle));

        // when
        Optional<Tag> tag = tagService.findByDateAndTagName(toDate("2016-10-01"), "health");

        // then
        assertTrue(tag.isPresent());
        assertNotNull(tag.get());
        assertEquals(tag.get().getTag(), "health");
        assertEquals(tag.get().getCount(), 1, 0);
        assertEquals(tag.get().getArticles(), Arrays.asList("1"));
        assertEquals(tag.get().getRelatedTags(), Sets.newHashSet("news"));
        verify(articleService, times(1)).findByDateAndTagNames(any(Date.class), any(String.class));
        verifyNoMoreInteractions(articleService);
    }

    @Configuration
    @ComponentScan({
            "com.ffx.blue.article.service",
            "com.ffx.blue.tag.service",
            "com.ffx.blue.tag.converter"
    })
    public static class TestAppConfig {
        @Bean
        public ArticleRepository articleRepository() {
            return null;
        }
    }
}