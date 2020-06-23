package com.domrade.spring.confg;

import static com.domrade.spring.confg.Application.LOGGER;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

// If you need only authorize or not a certain URL EnableWebSecurity will provide all you need 
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    static final Logger LOGGER = Logger.getLogger(MethodSecurityConfig.class.getName());

    @Bean
    public RoleHierarchy roleHierarchy() {
        LOGGER.log(Level.INFO, "#### public RoleHierarchy roleHierarchy()");
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_SUPERUSER > ROLE_ADMIN and ROLE_SUPERUSER > ROLE_SALESMAN and ROLE_ADMIN > ROLE_USER and ROLE_SALESMAN > ROLE_USER ");
        return roleHierarchy;
    }


    @Bean
    public DefaultMethodSecurityExpressionHandler methodSecurityExpressionHandler(){
        LOGGER.log(Level.INFO, "#### public DefaultMethodSecurityExpressionHandler methodSecurityExpressionHandler()");
        DefaultMethodSecurityExpressionHandler methodSecurityExpressionHandler = new DefaultMethodSecurityExpressionHandler();
        methodSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
        return methodSecurityExpressionHandler;
    }

    public static void main(String[] args) {
        LOGGER.log(Level.INFO, ">>>>>>>>>>>>>>>>#### public static void main2(String[] args)");
        StandardPasswordEncoder spe = new StandardPasswordEncoder("supersecret");
        System.out.println(spe.encode("password"));
    }
}
