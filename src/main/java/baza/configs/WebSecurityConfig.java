package baza.configs;

import baza.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;


/*
 Конфигурация Spring Security.
 Здесь описано:
 какие URL доступны каким ролям;
 как работает login/logout;
 чем кодировать пароли;
 откуда Spring Security должен брать пользователя при входе.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;
    private final UserService userService;

    public WebSecurityConfig(SuccessUserHandler successUserHandler, UserService userService) {
        this.successUserHandler = successUserHandler;
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                /*
                 CSRF-защита включена. Токен кладется в cookie XSRF-TOKEN,
                 а JavaScript отправляет его обратно в заголовке X-XSRF-TOKEN
                 для POST/PATCH/DELETE запросов.
                 */
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .authorizeRequests()
                // /user/** доступен обычному пользователю и админу.
                .antMatchers("/", "/user/**").hasAnyRole("USER", "ADMIN")
                // /admin/** доступен только админу.
                .antMatchers("/", "/admin/**").hasRole("ADMIN")
                .and()
                // После успешного логина редирект выбирает SuccessUserHandler.
                .formLogin().successHandler(successUserHandler)
                .permitAll()
                .and()
                // Logout разрешен авторизованным пользователям.
                .logout()
                .permitAll();
    }

    /*
     BCryptPasswordEncoder хранит пароли безопасно: в базе лежит hash,
     а не исходный пароль.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     DaoAuthenticationProvider говорит Spring Security:
     пользователей ищи через userService;
     пароли сравнивай через passwordEncoder.
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }
}
