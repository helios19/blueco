package com.ffx.blue.tag.converter;

import com.ffx.blue.article.domain.Article;
import com.ffx.blue.tag.domain.Tag;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static com.ffx.blue.common.utils.ClassUtils.toDate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ActiveProfiles({"test"})
@SpringApplicationConfiguration(classes = TagConverterTest.TestAppConfig.class,
        initializers = ConfigFileApplicationContextInitializer.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TagConverterTest {

    @Autowired
    private TagConverter tagConverter;

    private List<Article> articlesToConvert = Lists.newArrayList();


    @Before
    public void setUp() throws Exception {

        Article article1 = Article
                .newInstance()
                .withId("1")
                .withTitle("title of the first article")
                .withBody("body of first article")
                .withTags(Arrays.asList("news", "health"))
                .withDate(toDate("2016-10-01"));

        Article article2 = Article
                .newInstance()
                .withId("2")
                .withTitle("title of the second article")
                .withBody("body of second article")
                .withTags(Arrays.asList("health", "sport"))
                .withDate(toDate("2016-10-01"));

        Article article3 = Article
                .newInstance()
                .withId("3")
                .withTitle("title of the third article")
                .withBody("body of third article")
                .withTags(Arrays.asList("news", "health"))
                .withDate(toDate("2016-09-22"));

        articlesToConvert = Lists.newArrayList(article1, article2, article3);
    }

    @Test
    public void shouldConvertArticleListToTagObject() throws Exception {

        // when
        Tag tag = tagConverter.withTagName("health").convert(articlesToConvert);

        System.out.println("tag : " + tag);

        // then
        assertNotNull(tag);
        assertEquals(tag.getTag(), "health");
        assertEquals(tag.getArticles(), Arrays.asList("1", "2", "3"));
        assertEquals(tag.getCount(), 3, 0);
        assertEquals(tag.getRelatedTags(), Sets.newHashSet("news", "sport"));
    }

    @Configuration
    @ComponentScan({"com.ffx.blue.tag.converter"})
    public static class TestAppConfig {
    }
}

