package com.qlik.assignment.security;

import com.qlik.assignment.filter.CustomAuthenticationFilter;
import com.qlik.assignment.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfig extends WebSecurityConfigurerAdapter {
  private final UserDetailsService userDetailsService;

  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    CustomAuthenticationFilter CustomAuthenticationFilter =
        new CustomAuthenticationFilter(authenticationManagerBean());
    CustomAuthenticationFilter.setFilterProcessesUrl("/auth/user/login");
    http.csrf().disable();
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.authorizeHttpRequests()
        .antMatchers(
            "/auth/login/**",
            "/token/refresh?**",
            "/auth/user/register",
            "/auth/user/swagger-ui.html",
            "/auth/user/swagger-ui/index.html",
            "/auth/user/swagger-ui.html",
            "/favicon.ico",
            "/auth/user/swagger-ui/**",
            "/auth/user/api-docs/**",
            "/auth/user/api-docs")
        .permitAll();
    http.authorizeHttpRequests()
        .antMatchers(GET, "/auth/user/**")
        .hasAnyAuthority("ROLE_USER", "ROLE_ADMIN");
    http.authorizeHttpRequests().antMatchers(POST, "/auth/users").hasRole("ROLE_ADMIN");
    http.authorizeHttpRequests().antMatchers(POST, "/auth/role/addtouser");
    http.authorizeHttpRequests().anyRequest().authenticated();
    http.addFilter(CustomAuthenticationFilter);
    http.exceptionHandling().accessDeniedPage("/403");
    http.addFilterBefore(
        new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}
