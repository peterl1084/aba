package ch.abacus.application.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.vaadin.spring.security.config.AuthenticationManagerConfigurer;

@Configuration
public class VaadinAuthenticationManagerConfig implements AuthenticationManagerConfigurer {

    @Override
    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> inMemoryAuthentication = builder
                .inMemoryAuthentication();
        inMemoryAuthentication.withUser("admin").password("password").roles("ADMIN");
    }
}
