package com.codegym.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/home", "/login", "/register","/verify-otp", "/resources/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/cart/**", "/order/**", "/user/**").hasRole("USER")
                .anyRequest().authenticated()

                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/home", true)
                .permitAll()

                .and()
                .logout()
                .logoutUrl("/logout")
                .deleteCookies("JSESSIONID") // xóa cookie khi logout
                .logoutSuccessUrl("/home")
                .permitAll()

                .and()
                .sessionManagement()
                .maximumSessions(1)   // mỗi user chỉ cho login 1 session
                .maxSessionsPreventsLogin(false) // false = login mới sẽ kick session cũ
                .expiredUrl("/login?expired")    // nếu session hết hạn
                .sessionRegistry(sessionRegistry())

                .and().and()
                .rememberMe() // bật cookie remember-me
                .key("uniqueAndSecret") // key để mã hóa cookie
                .tokenValiditySeconds(7 * 24 * 60 * 60) // cookie sống 7 ngày
                .userDetailsService(userDetailsService)

                .and()
                .csrf().disable();
    }
}
