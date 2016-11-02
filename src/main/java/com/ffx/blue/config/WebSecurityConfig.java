package com.ffx.blue.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers()
                .contentTypeOptions().and()
                .xssProtection().and()
                .cacheControl().and()
                .httpStrictTransportSecurity().and()
                .frameOptions().and()
                .addHeaderWriter(new StaticHeadersWriter("X-Content-Security-Policy", "script-src 'self'"));

    }
}
