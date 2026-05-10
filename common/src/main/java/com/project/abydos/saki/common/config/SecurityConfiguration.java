package com.project.abydos.saki.common.config;

import com.project.abydos.saki.common.constant.Endpoint;
import com.project.abydos.saki.common.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security設定クラス.
 * ステートレスJWT認証の構成、エンドポイントごとのアクセス制御、パスワードエンコーダーの定義を行う。
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final CustomUserDetailService customUserDetailService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        var builder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder());

        return builder.build();
    }

    @Bean
    public SecurityFilterChain applicationSecurity(HttpSecurity http) throws Exception {

        http.addFilterBefore(jwtAuthenticationFilter, org.springframework.web.filter.CorsFilter.class);

        http.cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .securityMatcher("/**")
                .authorizeHttpRequests(
                        registry -> registry
                                .requestMatchers(Endpoint.ROOT).permitAll()
                                .requestMatchers(Endpoint.HEALTH).permitAll()
                                .requestMatchers(Endpoint.API_PREFIX + Endpoint.LOGIN).permitAll()
                                .anyRequest().authenticated()
                );

        return http.build();
    }
}
