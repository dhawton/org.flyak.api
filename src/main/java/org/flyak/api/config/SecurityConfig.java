package org.flyak.api.config;

import org.flyak.api.security.AuthTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api-docs/**", "/swagger-ui/**", "/").permitAll()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/user/{userId:\\d+}/**").hasRole("ADMIN")
                .antMatchers("/user/all").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/airport/**", "/airport").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/airport", "/airport/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/airline", "/airline/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/airline", "/airline/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/equipment", "/equipment/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/equipment", "/equipment/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/route", "/route/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/route", "/route/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/user").hasRole("ADMIN")
                .anyRequest().authenticated();

        httpSecurity.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManager();
    }
}
