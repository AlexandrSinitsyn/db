// package com.server.db.config;
//
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
//
// @SuppressWarnings("deprecation")
// @Configuration
// @EnableWebSecurity
// public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//     @Override
//     protected void configure(final HttpSecurity http) throws Exception {
//         http.authorizeRequests()
//                 .antMatchers("/swagger-ui/**", "/javainuse-openapi/**").permitAll()
//                 .anyRequest().authenticated()
//                 .and()
//                 .httpBasic();
//     }
//
//     @Override
//     protected void configure(final  AuthenticationManagerBuilder auth) throws Exception {
//         auth.inMemoryAuthentication()
//                 .withUser("javainuse")
//                 .password(passwordEncoder().encode("javainuse"))
//                 .authorities("ADMIN");
//     }
//
//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }
// }
