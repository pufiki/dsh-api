package ru.digitalsuperhero.dshapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.digitalsuperhero.dshapi.security.jwt.JwtConfigurer;
import ru.digitalsuperhero.dshapi.security.jwt.JwtTokenProvider;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    DataSource dataSource;

    @Autowired
    UserRepositoryUserDetailsService userRepositoryUserDetailsService;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/register/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers(HttpMethod.GET, "/restapi/customers/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/restapi/customers/**").hasRole("CUSTOMER")
                .antMatchers(HttpMethod.PUT, "/restapi/customers/**").hasRole("CUSTOMER")
                .antMatchers(HttpMethod.PATCH, "/restapi/customers/**").hasRole("CUSTOMER")
                .antMatchers(HttpMethod.GET, "/restapi/contractors/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/restapi/contractors/**").hasRole("CONTRACTOR")
                .antMatchers(HttpMethod.PUT, "/restapi/contractors/**").hasRole("CONTRACTOR")
                .antMatchers(HttpMethod.PATCH, "/restapi/contractors/**").hasRole("CONTRACTOR")
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider))
                // Allow pages to be loaded in frames from the same origin; needed for H2-Console
                .and()
                .headers()
                .frameOptions()
                .sameOrigin();

//        http
//                .httpBasic()
//                .realmName("dsh-api")
//
//                .and()
//                .csrf()
//                .ignoringAntMatchers("/h2-console/**", "/register/**", "/login/**", "/restapi/**")
//
//                // Allow pages to be loaded in frames from the same origin; needed for H2-Console
//                .and()
//                .headers()
//                .frameOptions()
//                .sameOrigin()
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userRepositoryUserDetailsService)
                .passwordEncoder(encoder());
    }
}
