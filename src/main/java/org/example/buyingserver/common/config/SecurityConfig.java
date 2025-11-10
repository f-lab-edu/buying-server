package org.example.buyingserver.common.config;

import lombok.RequiredArgsConstructor;
import org.example.buyingserver.common.auth.JwtTokenFilter;
import org.example.buyingserver.common.dto.ErrorCodeAndMessage;
import org.example.buyingserver.member.service.CustomOAuth2UserService;
import org.example.buyingserver.common.auth.OAuth2SuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/member/create",
                                "/member/login",
                                "/member/google/*",   // 후에 삭제해놔야함
                                "/oauth2/**",
                                "/login/oauth2/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/favicon.ico",
                                "/posts",
                                "/posts/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                // OAuth2 로그인 구성
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler((request, response, e) -> {
                            System.out.println("[DEBUG] : OAuth2 로그인 실패 " + e.getMessage());
                            ErrorCodeAndMessage error = ErrorCodeAndMessage.UNAUTHORIZED;
                            response.setStatus(error.getCode());
                            response.setContentType("text/plain; charset=UTF-8");
                            response.getWriter().write(error.getMessage());
                            response.getWriter().flush();
                        })
                );

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}