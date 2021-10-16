package com.solomatoff.job4jurlshortcut.config;

import com.solomatoff.job4jurlshortcut.filter.JWTAuthenticationFilter;
import com.solomatoff.job4jurlshortcut.filter.JWTAuthorizationFilter;
import com.solomatoff.job4jurlshortcut.jwt.TokenAuthenticationService;
import com.solomatoff.job4jurlshortcut.service.ISiteService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenAuthenticationService tokenAuthenticationService;
    private final ISiteService siteService;

    public SecurityConfig(UserDetailsService userDetailsService,
                          BCryptPasswordEncoder bCryptPasswordEncoder,
                          TokenAuthenticationService tokenAuthenticationService, ISiteService siteService) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.siteService = siteService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(
                        "/api/v1/registration",
                        "/api/v1/site/**",
                        "/api/v1/redirect/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(
                        authenticationManager(),
                        tokenAuthenticationService,
                        siteService)
                )
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), tokenAuthenticationService))
                /* this disables session creation on Spring Security */
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}