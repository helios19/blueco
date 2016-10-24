package com.ffx.blue.article.controller;

import com.ffx.blue.article.domain.Article;
import com.ffx.blue.article.service.ArticleService;
import com.google.common.collect.Lists;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static com.ffx.blue.common.utils.ClassUtils.toDate;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MockServletContext.class)
public class ArticleControllerTest {

    @InjectMocks
    ArticleController controller;

    @Mock
    private ArticleService service;

    private MockMvc mvc;

    private Article article = Article
            .newInstance()
            .withId("1")
            .withVersion(1l)
            .withTitle("title test")
            .withBody("body test")
            .withTags(Arrays.asList("news", "sport"))
            .withDate(toDate("2016-10-01"));

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
        RestAssuredMockMvc.mockMvc(mvc);
    }

    @Test
    public void shouldAddArticle() throws Exception {
        String articleJson = json(article);

        given().
                contentType(ContentType.JSON).
                body(articleJson).
                when().
                post("/articles").
                then().
                statusCode(HttpServletResponse.SC_CREATED);
    }

    @Test
    public void shouldFindById() {

        when(service.findById(any(String.class)))
                .thenReturn(Optional.of(article));

        given().
                when().
                get("/articles/1").
                then().
                statusCode(HttpServletResponse.SC_OK).
                contentType(ContentType.JSON).
                body("id", equalTo("1")).
                body("body", equalTo("body test")).
                body("tags", equalTo(Arrays.asList("news", "sport"))).
                body("date", equalTo("20160930")).
                log().all(true);

        verify(service, times(1)).findById(any(String.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldFindByAll() {

        when(service.findAll(any()))
                .thenReturn(Lists.newArrayList(article));

        given().
                when().
                get("/articles").
                then().
                statusCode(HttpServletResponse.SC_OK).
                contentType("application/json").
                body("size()", equalTo(1)).
                body("[0].id", equalTo("1")).
                body("[0].body", equalTo("body test")).
                body("[0].tags", equalTo(Arrays.asList("news", "sport"))).
                body("[0].date", equalTo("20160930")).
                log().all(true);

        verify(service, times(1)).findAll(any());
        verifyNoMoreInteractions(service);
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        new MappingJackson2HttpMessageConverter().write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
