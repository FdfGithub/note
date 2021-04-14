package com.qianjing.note;

import com.qianjing.note.conf.HttpFilter;
import com.qianjing.note.conf.HttpInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@SpringBootApplication
@EnableScheduling
public class NoteApplication extends WebMvcConfigurationSupport {

    public static void main(String[] args) {
        SpringApplication.run(NoteApplication.class, args);
    }

    @Value("${localAddress_pre}")
    private String localAddressPre;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:"+localAddressPre+"");
//                .addResourceLocations("file:F:/upload/");
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HttpInterceptor())
                .addPathPatterns("/**");
    }


    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean frBean = new FilterRegistrationBean();
        frBean.setFilter(new HttpFilter());
        frBean.addUrlPatterns("/*");
        return frBean;
    }

}
