package com.domrade.spring.confg;

import com.domrade.security.PBKDF2Hasher;
import com.domrade.service.implementation.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
//@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    static final Logger LOGGER = Logger.getLogger(Application.class.getName());

    // Bean is defined in MehtodSecurityConfig.java
    @Autowired
    private RoleHierarchy roleHierarchy;

    @Bean(name = "standardPasswordEncoder")
    public PasswordEncoder standardPasswordEncoder() {
        LOGGER.log(Level.INFO, "public PasswordEncoder standardPasswordEncoder()");
        return new StandardPasswordEncoder("supersecret");
    }

    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        LOGGER.log(Level.INFO, "public UserDetailsService userDetailsService()");
        return new UserLoginService();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        LOGGER.log(Level.INFO, "public void configureGlobal(AuthenticationManagerBuilder auth)");
        auth.userDetailsService(userDetailsService()).passwordEncoder(pbkdf2Hasher());
    }

    @Bean
    public RoleHierarchyVoter roleHierarchyVoter() {
        LOGGER.log(Level.INFO, "public RoleHierarchyVoter roleHierarchyVoter()");
        return new RoleHierarchyVoter(roleHierarchy);
    }

    @Bean
    public WebExpressionVoter webExpressionVoter() {
        LOGGER.log(Level.INFO, "public WebExpressionVoter webExpressionVoter()");
        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
        DefaultWebSecurityExpressionHandler webSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        webSecurityExpressionHandler.setRoleHierarchy(roleHierarchy);
        webExpressionVoter.setExpressionHandler(webSecurityExpressionHandler);
        return webExpressionVoter;
    }

    @Bean
    public AffirmativeBased accessDecisionManager() {
        LOGGER.log(Level.INFO, "public AffirmativeBased accessDecisionManager()");
        List<AccessDecisionVoter<? extends Object>> decisionVoterList = new ArrayList<>();
        decisionVoterList.add(roleHierarchyVoter());
        decisionVoterList.add(webExpressionVoter());
        return new AffirmativeBased(decisionVoterList);
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    @Bean
    public PBKDF2Hasher pbkdf2Hasher() {
        return new PBKDF2Hasher();
    }

    @Bean
    public BCryptPasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        LOGGER.log(Level.INFO, "protected void configure(HttpSecurity http)");
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/secured/view/**").fullyAuthenticated()
                .antMatchers("/secured/admin/**", "/secured/view/admin/**").access("hasRole('ROLE_SUPERUSER')")
                .antMatchers("/index.xhtml", "/index.html", "/login.xhtml", "/javax.faces.resources/**").permitAll()
                //.and()
                //.formLogin()
                .and()
                .logout().logoutSuccessUrl("/login.xhtml").invalidateHttpSession(true).deleteCookies("JSESSIONID")
                .and()
                .exceptionHandling().accessDeniedPage("/login.xhtml")
                .and()
                .sessionManagement()
                .sessionFixation().newSession()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .invalidSessionUrl("/login.xhtml")
                .maximumSessions(1)
                .expiredUrl("/login.xhtml");

    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
