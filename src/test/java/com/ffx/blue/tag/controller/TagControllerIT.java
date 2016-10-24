package com.ffx.blue.tag.controller;

import com.ffx.blue.Application;
import com.ffx.blue.article.domain.Article;
import com.ffx.blue.article.repository.ArticleRepository;
import com.ffx.blue.tag.service.TagService;
import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodExecutable;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.Arrays;

import static com.ffx.blue.common.utils.ClassUtils.COUNTERS_COLLECTION_NAME;
import static com.ffx.blue.common.utils.ClassUtils.toDate;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
@Ignore
public class TagControllerIT {

    private MockMvc mockMvc;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Autowired
    private MongodExecutable mongodExecutable;

    @Autowired
    private MongoClient mongo;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TagService tagService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {

        mockMvc = webAppContextSetup(webApplicationContext).build();

        Article article1 = Article
                .newInstance()
                .withTitle("title of the first article")
                .withBody("body of first article")
                .withTags(Arrays.asList("news", "health"))
                .withDate(toDate("2016-10-01"));

        Article article2 = Article
                .newInstance()
                .withTitle("title of the second article")
                .withBody("body of second article")
                .withTags(Arrays.asList("news", "sport"))
                .withDate(toDate("2016-10-01"));

        Article article3 = Article
                .newInstance()
                .withTitle("title of the third article")
                .withBody("body of third article")
                .withTags(Arrays.asList("news", "health"))
                .withDate(toDate("2016-09-22"));

        articleRepository.saveOrUpdate(article1, article2, article3);
    }

    @After
    public void tearDown() throws Exception {
        // reset article collection
        articleRepository.getMongoTemplate().dropCollection(Article.class);
        // reset sequence collection
        articleRepository.getMongoTemplate().dropCollection(COUNTERS_COLLECTION_NAME);
        // stop mongodb server
        mongo.close();
        mongodExecutable.stop();
    }

    @Test
    public void shouldFindTagByNameAndDate() throws Exception {

        mockMvc.perform(get("/tags/health/20160922"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.tag", is("health")))
                .andExpect(jsonPath("$.count", is(1)))
                .andExpect(jsonPath("$.articles", is(Arrays.asList("3"))))
                .andExpect(jsonPath("$.related_tags", is(Arrays.asList("news"))));
    }
}