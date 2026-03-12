package com.sweetbite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Security配置
 * 暂时禁用认证，方便测试
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF
            .csrf().disable()
            // 允许所有请求
            .authorizeRequests()
            .antMatchers(
                "/api/**",
                "/doc.html",
                "/doc.html/**",
                "/swagger-resources/**",
                "/webjars/**",
                "/v2/api-docs",
                "/v3/api-docs",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/favicon.ico",
                "/knife4j/**"
            ).permitAll()
            .anyRequest().permitAll();
    }
}
