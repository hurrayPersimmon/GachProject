package com.f2z.gach.Config;


import com.f2z.gach.Auth.CustomAccessDeniedHandler;
import com.f2z.gach.Auth.CustomPasswordEncoder;
import com.f2z.gach.Auth.CustomUserDetailService;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

    private final CustomUserDetailService customUserDetailService;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public PasswordEncoder PasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/", "/admin/main-page");
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();
        accessDeniedHandler.setErrorURL("/admin/deny");

        return accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/images/**","/css/**", "/image/**", "/js/**" ,"/").permitAll()
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN", "GUEST")
                        .requestMatchers("/user/login").permitAll()
                        .requestMatchers("/admin/main-page").permitAll()
                        .requestMatchers("/admin/signup").permitAll()
                        .requestMatchers("/user/signup?**").permitAll()
                        .requestMatchers("/user/signup").permitAll()
                        .requestMatchers("/user/guest").permitAll()
                        .requestMatchers("/user/check-password").permitAll()
                        .requestMatchers("/user/**").permitAll()
                        .requestMatchers("/event/list").permitAll()
                        .requestMatchers("/event/**").permitAll()
                        .requestMatchers("/map/building-info/list").permitAll()
                        .requestMatchers("/map/building-floor/**").permitAll()
                        .requestMatchers("/map/**").permitAll()
                        .requestMatchers("/map/find?**").permitAll()
                        .requestMatchers("map/route-now/**").permitAll()
                        .requestMatchers("/map/ar").permitAll()
                        .requestMatchers("/inquiry/list/**").permitAll()
                        .requestMatchers("/inquiry/**").permitAll()
                        .requestMatchers("/inquiry").permitAll()
                        .requestMatchers("/history/list/**").permitAll()
                        .requestMatchers("/history").permitAll()
                        .requestMatchers("/history/top-nodes").permitAll()
                        .requestMatchers("/admin/list").hasRole("ADMIN")
                )
                .formLogin(login -> login
                        .loginPage("/admin/login")
                        .loginProcessingUrl("/login-process")
                        .usernameParameter("adminId")
                        .passwordParameter("adminPassword")
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .permitAll()
                )
                .logout(out -> out.logoutUrl("/"))
                .userDetailsService(customUserDetailService)
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(
                        http2 -> http2.requestMatchers("/").permitAll()
                                .requestMatchers("/admin/main-page").permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling((exceptionConfig) ->
                        exceptionConfig.accessDeniedHandler(accessDeniedHandler).accessDeniedPage("/admin/deny")
                );
        return http.build();
    }
}