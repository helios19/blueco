package com.ffx.blue.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ffx.blue.common.security.RateLimiterFilter;
import com.ffx.blue.common.security.XssFilter;
import io.undertow.UndertowOptions;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


/**
 * Configuration class enabling web filers such as {@link ShallowEtagHeaderFilter}
 */
@EnableWebMvc
@EnableSpringDataWebSupport
@Configuration
public class WebConfig {

    /**
     * Returns an {@link FilterRegistrationBean} instance wrapping a {@link ShallowEtagHeaderFilter} filter
     * to be loaded by the spring context.
     *
     * @param shallowEtagHeaderFilter Spring ETag filter
     * @return FilterRegistrationBean instance
     */
    @Bean
    public FilterRegistrationBean someFilterRegistration(ShallowEtagHeaderFilter shallowEtagHeaderFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(shallowEtagHeaderFilter);
        registration.setOrder(1);
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Bean
    public ShallowEtagHeaderFilter shallowEtagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }

    /**
     * Returns an {@link FilterRegistrationBean} instance wrapping a {@link XssFilter} filter
     * to be loaded by the spring context.
     *
     * @return FilterRegistrationBean instance
     */
    @Bean
    public FilterRegistrationBean xssFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        XssFilter xssFilter = new XssFilter();
        registrationBean.setFilter(xssFilter);
        registrationBean.setOrder(1);
        return registrationBean;
    }

    /**
     * Return an {@link FilterRegistrationBean} instance wrapping a {@link RateLimiterFilter} filter
     * to be loaded by the spring context.
     *
     * @return FilterRegistrationBean instance
     */
    @Bean
    public FilterRegistrationBean rateLimiterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        RateLimiterFilter limiterFilter = new RateLimiterFilter();
        registrationBean.setFilter(limiterFilter);
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public UndertowEmbeddedServletContainerFactory embeddedServletContainerFactory() {
        UndertowEmbeddedServletContainerFactory factory = new UndertowEmbeddedServletContainerFactory();
        factory.addBuilderCustomizers(
                builder -> builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true));
        return factory;
    }

    /**
     * Returns a Jackson message converter customized with the following parameters:
     * <li> - {@link DeserializationFeature#FAIL_ON_UNKNOWN_PROPERTIES} disabled
     * <li> - {@link SerializationFeature#WRITE_DATES_AS_TIMESTAMPS} disabled
     *
     * @return MappingJackson2HttpMessageConverter
     */
    @Bean
    @Primary
    public MappingJackson2HttpMessageConverter jacksonConvertor() {
        MappingJackson2HttpMessageConverter converter= new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        converter.setObjectMapper(mapper);
        return converter;
    }

}
