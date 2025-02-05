package com.colour.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.setMatchingRequestParameterName("testLogin=y");

        http
                .authorizeRequests(auth -> auth
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) ->
                        {
                            SavedRequest savedRequest = requestCache.getRequest(request, response);
                            String redirectUrl = savedRequest.getRedirectUrl();
                            response.sendRedirect(redirectUrl);
                        })
                        .failureUrl("/login")
                        .permitAll())
                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .cors(Customizer.withDefaults())
        ;
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails exampleUser = User.withUsername("user").password("{noop}1234").roles("USER").build();
        return new InMemoryUserDetailsManager(exampleUser);
    }

}
