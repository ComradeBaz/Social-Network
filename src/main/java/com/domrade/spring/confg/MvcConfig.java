/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.spring.confg;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author ComradeBaz
 */
@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

    private static final Logger LOGGER = Logger.getLogger(MvcConfig.class);

    @Override
    // Add resources for serving static user profile pictures
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/javax.faces.resource/**", "src/main/resources/**", "/secured/view/src/main/resources/**", "/resources/static/**", "/app/resources/static/**")
                .addResourceLocations("file:///C:\\Users\\David\\Documents\\NetBeansProjects\\hostel_xn\\src\\main\\resources\\**",
                        "file:///C:\\Users\\David\\Documents\\NetBeansProjects\\hostel_xn\\src\\main\\resources\\static\\",
                        "file:///C:\\apache-tomcat-9.0.26\\bin\\src\\main\\resources\\**",
                        "/META-INF/resources/", "file:/app/resources/static/", "file:/resources/static/", "file:/app/resources/static/**");
    }
}
