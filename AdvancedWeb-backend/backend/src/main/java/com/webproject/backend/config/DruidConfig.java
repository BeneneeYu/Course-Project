package com.webproject.backend.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.sql.DataSource;

/**
 * @description
 * Druid数据源配置
 */
@Configuration
public class DruidConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSource druidDataSource(){
        return new DruidDataSource();
    }

    /**
     * @description
     * 后台监控
     * 登录url:localhost:8080/druid/index.html
     * 设置了用户名和密码
     * @return
     */
    @Bean
    public ServletRegistrationBean<Servlet> statViewServlet(){
        ServletRegistrationBean<Servlet> registrationBean = new ServletRegistrationBean<>(new StatViewServlet(),"/druid/*");
        registrationBean.addInitParameter("allow", "0.0.0.0");

        registrationBean.addInitParameter("loginUsername", "webproject");
        registrationBean.addInitParameter("loginPassword", "123456");

        registrationBean.addInitParameter("resetEnable", "false");

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> statFilter(){
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>(new WebStatFilter());
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }
}
