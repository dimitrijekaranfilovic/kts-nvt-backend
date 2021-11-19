package com.ktsnvt.ktsnvt.config;


import com.ktsnvt.ktsnvt.config.jwt.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final JwtTokenFilter jwtTokenFilter;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, JwtTokenFilter jwtTokenFilter, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenFilter = jwtTokenFilter;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(this.passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //mora se provjeriti web socket controller
        http = http.cors().and().csrf().disable();

        http = http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();

        http = http.exceptionHandling().authenticationEntryPoint((req, resp, ex) ->
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage())).and();

        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/super-users/authenticate").permitAll()
                .antMatchers(HttpMethod.POST, "/api/orders").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/orders/{id}/charge").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/orders/{id}/cancel").permitAll()
                .antMatchers(HttpMethod.POST, "/api/orders/{id}/groups").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/orders/{orderId}/groups/{groupId}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/orders/{id}/groups").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/orders/{orderId}/groups/{groupId}").permitAll()

                .antMatchers(HttpMethod.GET, "/api/order-items/requests").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/order-items/take").permitAll()
                .antMatchers(HttpMethod.POST, "/api/order-items").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/order-items/{orderItemId}").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/order-items/{orderItemId}").permitAll()

                .antMatchers(HttpMethod.GET, "/api/sections/{sectionId}/tables").permitAll()
                .antMatchers(HttpMethod.GET, "/api/sections").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(jwtTokenFilter,
                UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                //dodati kasnije allowedOrigins
                registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS");
            }
        };
    }
}
