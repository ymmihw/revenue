package com.whimmy.revenue.web.config;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.CssLinkResourceTransformer;
import org.springframework.web.servlet.resource.VersionResourceResolver;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import nz.net.ultraq.thymeleaf.LayoutDialect;

@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {

  private static final String ENCODING = "UTF-8";

  @Autowired
  @Bean
  public ServletContextTemplateResolver templateResolver(ServletContext servletContext) {
    ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
    resolver.setPrefix("/WEB-INF/template/");
    resolver.setSuffix(".html");
    resolver.setTemplateMode("HTML5");
    resolver.setCacheable(false);
    return resolver;
  }

  @Autowired
  @Bean
  public SpringTemplateEngine templateEngine(ServletContextTemplateResolver templateResolver) {
    SpringTemplateEngine engine = new SpringTemplateEngine();
    engine.setTemplateResolver(templateResolver);
    // html中使用layout方言
    engine.addDialect(new LayoutDialect());
    engine.setAdditionalDialects(additionalDialects());
    return engine;
  }

  @Autowired
  @Bean
  public ThymeleafViewResolver thymeleafViewResolver(SpringTemplateEngine templateEngine) {
    ThymeleafViewResolver resolver = new ThymeleafViewResolver();
    resolver.setCharacterEncoding(ENCODING);
    resolver.setContentType("text/html; charset=UTF-8");
    resolver.setTemplateEngine(templateEngine);
    return resolver;
  }

  private Set<IDialect> additionalDialects() {
    Set<IDialect> dialects = new HashSet<IDialect>();
    dialects.add(new SpringSecurityDialect());
    return dialects;
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/resource/**").addResourceLocations("/resource/")
        .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS)).resourceChain(true)
        .addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"))
        .addTransformer(new CssLinkResourceTransformer());
    registry.addResourceHandler("/template/**").addResourceLocations("/WEB-INF/template/")
        .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS)).resourceChain(true)
        .addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"))
        .addTransformer(new CssLinkResourceTransformer());
  }

}
