package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SuccessUserHandler successUserHandler;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(SuccessUserHandler successUserHandler,
                          UserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder) {
        this.successUserHandler = successUserHandler;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // Для API отключение CSRF может быть целесообразным. Для форм можно включить.
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers("/login", "/auth/**", "/css/**", "/js/**", "/images/**").permitAll()  // Разрешаем доступ к публичным ресурсам
                .antMatchers("/admin/**").hasRole("ADMIN")  // Путь /admin для админов
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")  // Путь /user для пользователей и админов
                .anyRequest().authenticated()  // Все остальные пути требуют авторизации
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler(successUserHandler)  // Редирект после успешного логина
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }
}
