package com.ffx.blue.tag.controller;

import com.ffx.blue.tag.domain.Tag;
import com.ffx.blue.tag.service.TagService;
import com.google.common.collect.Sets;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MockServletContext.class)
public class TagControllerTest {

    @InjectMocks
    TagController controller;

    @Mock
    private TagService service;

    private MockMvc mvc;

    private Tag tag = Tag
            .newInstance()
            .withTag("health")
            .withArticles(Arrays.asList("1"))
            .withCount(1)
            .withRelatedTags(Sets.newHashSet("news", "sport"));

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
        RestAssuredMockMvc.mockMvc(mvc);
    }

    @Test
    public void shouldFindByTagNameAndDate() {

        when(service.findByDateAndTagName(any(Date.class), any(String.class)))
                .thenReturn(Optional.of(tag));

        given().
                when().
                get("/tags/health/20160922").
                then().
                statusCode(HttpServletResponse.SC_OK).
                contentType(ContentType.JSON).
                body("tag", equalTo("health")).
                body("count", equalTo(1)).
                body("articles", equalTo(Arrays.asList("1"))).
                body("related_tags", equalTo(Arrays.asList("news", "sport"))).
                log().all(true);

        verify(service, times(1)).findByDateAndTagName(any(Date.class), any(String.class));
        verifyNoMoreInteractions(service);
    }

}