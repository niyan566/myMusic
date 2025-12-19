package com.example.config;

import com.example.interceptor.LoginInterceptor;
import com.example.interceptor.MusicInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Resource
    private LoginInterceptor loginInterceptor;

    @Resource
    private MusicInterceptor musicInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/user/**","/recentlyPlayed/**","/playList/add")//要拦截的请求
                .excludePathPatterns("/user/login");//不拦截的请求

        registry.addInterceptor(musicInterceptor)
                .addPathPatterns("/music/**");
    }
}
