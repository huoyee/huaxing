package com.wechat;

import javax.servlet.ServletException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.google.code.kaptcha.servlet.KaptchaServlet;
import com.wechat.web.CommonInterceptor;

@SpringBootApplication
public class IndexAppliction extends WebMvcConfigurerAdapter{
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new CommonInterceptor()).addPathPatterns("/**");
	}
	
    // set defaut locale en_US
    @Bean   
    public ServletRegistrationBean servletRegistrationBean() throws ServletException{  
        return new ServletRegistrationBean(new KaptchaServlet(),"/static/images/kaptcha.jpg");  
    }  
	
	public static void main(String[] args) {
        SpringApplication.run(IndexAppliction.class, args);
    }
}
