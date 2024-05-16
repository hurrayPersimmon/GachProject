package com.f2z.gach.Config;


import com.f2z.gach.Auth.CustomPasswordEncoder;
import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests(request -> request
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                        .requestMatchers("/image/**","/css/**").permitAll()
                        .requestMatchers("/user/login").permitAll()
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
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/admin/login")
                        .loginProcessingUrl("/admin/login")
                        .usernameParameter("adminId")
                        .passwordParameter("adminPassword")
                        .defaultSuccessUrl("/admin/node/list/0", true)
                        .permitAll()
                )
                .logout(out -> out.logoutRequestMatcher(new AntPathRequestMatcher("/logout")))
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public CustomPasswordEncoder PasswordEncoder(){
        return new CustomPasswordEncoder();
    }
}