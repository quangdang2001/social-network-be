package com.cnpm.socialmedia.config;

import com.cnpm.socialmedia.fillter.UserAuthenticationFilter;
import com.cnpm.socialmedia.fillter.UserAuthorizationFilter;
import com.cnpm.socialmedia.utils.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final String[] WHITE_LIST_URLS = {
            "/api/register",
            "/api/resetPassword",
            "/api/verifyRegistration*",
            "/api/savePassword",
            "/api/refreshToken",
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/ws/**"
    };

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        UserAuthenticationFilter userAuthenticationFilter= new UserAuthenticationFilter(authenticationManagerBean());
        userAuthenticationFilter.setFilterProcessesUrl("/api/login");
        http.csrf().disable();
        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers(WHITE_LIST_URLS).permitAll();
        http
                .authorizeHttpRequests()
                .antMatchers("/ws/**").permitAll();
        http
                .authorizeRequests()
                        .antMatchers("/api/**").hasAnyAuthority(Constant.ROLE_USER,Constant.ROLE_ADMIN)
                        .antMatchers("/admin/**").hasAnyAuthority(Constant.ROLE_ADMIN);

        http.addFilter(userAuthenticationFilter);
        http.addFilterBefore(new UserAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

//        http
//                .authorizeHttpRequests()
//                .antMatchers(new String[]{"/", "/not-restricted"}).permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .oauth2Login();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
