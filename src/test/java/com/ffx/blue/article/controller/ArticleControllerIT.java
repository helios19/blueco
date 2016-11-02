package com.ffx.blue.article.controller;

import com.ffx.blue.Application;
import com.ffx.blue.article.domain.Article;
import com.ffx.blue.article.repository.ArticleRepository;
import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodExecutable;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Optional;

import static com.ffx.blue.common.utils.ClassUtils.COUNTERS_COLLECTION_NAME;
import static com.ffx.blue.common.utils.ClassUtils.toDate;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
@Ignore
public class ArticleControllerIT {

	private MockMvc mockMvc;

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private MongodExecutable mongodExec;

	@Autowired
	private MongoClient mongoClient;

	private static MongodExecutable staticMongodExec;
	private static MongoClient staticMongoClient;

    @Before
    public void setUp() throws Exception {

		mockMvc = webAppContextSetup(webApplicationContext).build();

		Article article1 = Article
				.newInstance()
				.withTitle("title of the first article")
				.withBody("body of first article")
				.withTags(Arrays.asList("news", "sport"))
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
				.withTags(Arrays.asList("news", "sport"))
				.withDate(toDate("2016-10-01"));

		articleRepository.saveOrUpdate(article1, article2, article3);

		// init mongodb
		staticMongodExec = mongodExec;
		staticMongoClient = mongoClient;
	}

    @After
    public void tearDown() throws Exception {
		// reset article collection
		articleRepository.getMongoTemplate().dropCollection(Article.class);
		// reset sequence collection
		articleRepository.getMongoTemplate().dropCollection(COUNTERS_COLLECTION_NAME);
	}

	@AfterClass
	public static void postContruct() {
		// stop mongodb
		staticMongoClient.close();
		staticMongodExec.stop();
	}

	@Test
	public void shouldAddArticle() throws Exception {
		String articleJson = json(Article
				.newInstance()
				.withTitle("title of the fourth article")
				.withBody("body of the fourth article")
				.withTags(Arrays.asList("news", "sport"))
				.withDate(toDate("2016-10-01")));

		this.mockMvc.perform(post("/articles")
				.contentType(contentType)
				.content(articleJson))
				.andDo(print())
				.andExpect(status().isCreated());

		// verify article record has been saved
		Optional<Article> article = articleRepository.findById("4");

		// assert presence of article saved
		assertTrue(article.isPresent());
	}

	@Test
	public void shouldFindArticleById() throws Exception {
		mockMvc.perform(get("/articles/1"))
				.andDo(print())
				.andExpect(status().isOk()).andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.id", is("1")))
				.andExpect(jsonPath("$.body", is("body of first article")))
				.andExpect(jsonPath("$.tags", is(Arrays.asList("news", "sport"))))
				.andExpect(jsonPath("$.date", is("20160930")))
				.andExpect(header().string("ETag", notNullValue()));
	}

	@Test
	public void shouldFindAllArticle() throws Exception {

		mockMvc.perform(get("/articles"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$[0].id", is("1")))
				.andExpect(jsonPath("$[0].body", is("body of first article")))
				.andExpect(jsonPath("$[0].tags", is(Arrays.asList("news", "sport"))))
				.andExpect(jsonPath("$[0].date", is("20160930")))
				.andExpect(jsonPath("$[1].id", is("2")))
				.andExpect(jsonPath("$[1].body", is("body of second article")))
				.andExpect(jsonPath("$[1].tags", is(Arrays.asList("news", "sport"))))
				.andExpect(jsonPath("$[1].date", is("20160930")))
				.andExpect(jsonPath("$[2].id", is("3")))
				.andExpect(jsonPath("$[2].body", is("body of third article")))
				.andExpect(jsonPath("$[2].tags", is(Arrays.asList("news", "sport"))))
				.andExpect(jsonPath("$[2].date", is("20160930")))
				.andReturn();
	}

	@Test
	public void shouldThrowExceptionWhenArticleIsNotFound() throws Exception {
		String unknownArticleId = "1111";

		mockMvc.perform(get("/articles/" + unknownArticleId))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$[0].logref", is("error")))
				.andExpect(jsonPath("$[0].message", is("Article not found with id:" + unknownArticleId)));
	}

	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		new MappingJackson2HttpMessageConverter().write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}

}
