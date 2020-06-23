package com.domrade.spring.confg;

import com.domrade.sse.ManageServerSentEvents;
import com.domrade.sse.MySseClient;
import org.primefaces.webapp.filter.FileUploadFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.faces.webapp.FacesServlet;
import javax.servlet.DispatcherType;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.reactive.function.client.WebClient;

@Import({MethodSecurityConfig.class, WebSecurityConfig.class, MvcConfig.class})
@ComponentScan(basePackages = {
    "com.domrade.service",
    "com.domrade.cache.service",
    "com.domrade.controllers",
    "com.domrade.jsf",
    "com.domrade.jms",
    "com.domrade.sse",
    "com.domrade.test.sse",
    "com.domrade.jms.config",
    "com.domrade.jsf.comfig",
    "com.domrade.rest.controllers",
    "com.domrade.session.worker.beans"})
@EntityScan(basePackages = {"com.domrade.domain"})
@EnableJpaRepositories(basePackages = {"com.domrade.repository"})
@SpringBootApplication
public class Application extends SpringBootServletInitializer implements ApplicationRunner {

    static final Logger LOGGER = Logger.getLogger(Application.class.getName());

    @Autowired
    private TaskExecutor taskExecutor;

    public static void main(String[] args) {
        LOGGER.log(Level.INFO, "public static void main(String[] args)");
        SpringApplication app = new SpringApplication(Application.class);

        app.run(args);

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOGGER.log(Level.INFO, "run(ApplicationArguments args) throws Exception");
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

    @Bean
    WebClient webClient() {
        return WebClient.create("http://192.168.1.128:8080/services/api/stats/broadcast");
    }
    // Register the faces Servlet
    // https://stackoverflow.com/questions/36421161/creating-a-second-servlet-using-spring-boot
    @Bean
    public ServletRegistrationBean facesServlet() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new FacesServlet(), new String[]{"*.xhtml"});
        registration.setName("Faces Servlet");
        registration.setLoadOnStartup(1);
        return registration;
    }

    /* 
    A ServletContextInitializer to register Filters in a Servlet 3.0+ container. 
    This is a PrimeFaces component used to manage uploading files
    The initial dispatcher type of a request is defined as DispatcherType.REQUEST. 
    The dispatcher type of a request dispatched via RequestDispatcher.forward(ServletRequest, 
    ServletResponse) or RequestDispatcher.include(ServletRequest, ServletResponse) 
    is given as DispatcherType.FORWARD or DispatcherType.INCLUDE, respectively, 
    while the dispatcher type of an asynchronous request dispatched via one of the 
    AsyncContext.dispatch() methods is given as DispatcherType.ASYNC. Finally, 
    the dispatcher type of a request dispatched to an error page by the container's 
    error handling mechanism is given as DispatcherType.ERROR.
     */
    @Bean
    public FilterRegistrationBean facesUploadFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new FileUploadFilter(), facesServlet());
        registrationBean.setName("PrimeFaces FileUpload Filter");
        registrationBean.addUrlPatterns("/*");
        registrationBean.setDispatcherTypes(DispatcherType.FORWARD, DispatcherType.REQUEST);
        return registrationBean;
    }

    //ErrorPageRegistration, mapping method
    @Bean
    public ErrorPageRegistrar errorPageRegistrar() {
        String defaultErrorPage = "/error.xhtml";
        return new ErrorPageRegistrar() {
            @Override
            public void registerErrorPages(ErrorPageRegistry registry) {
                registry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, defaultErrorPage));
                registry.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, defaultErrorPage));
                registry.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, defaultErrorPage));
            }
        };
    }

    @Bean
    public ServletContextInitializer servletContextInitializer() {
        return servletContext -> {
            // JSF
            servletContext.setInitParameter("com.sun.faces.forceLoadConfiguration", Boolean.TRUE.toString());
            servletContext.setInitParameter("javax.faces.FACELETS_SKIP_COMMENTS", Boolean.TRUE.toString());

            // PRIMEFACES
            servletContext.setInitParameter("primefaces.THEME", "bootstrap");
            servletContext.setInitParameter("primefaces.CLIENT_SIDE_VALIDATION", Boolean.TRUE.toString());
            servletContext.setInitParameter("primefaces.FONT_AWESOME", Boolean.TRUE.toString());
            servletContext.setInitParameter("primefaces.UPLOADER", "commons");
            // BOOTSFACES
            servletContext.setInitParameter("net.bootsfaces.get_fontawesome_from_cdn", Boolean.TRUE.toString());
            servletContext.setInitParameter("BootsFaces_USETHEME", Boolean.TRUE.toString());
        };
    }

    @Bean(name = "taskExecutor")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(10);
        taskExecutor.afterPropertiesSet();
        return new ThreadPoolTaskExecutor();
    }
}
