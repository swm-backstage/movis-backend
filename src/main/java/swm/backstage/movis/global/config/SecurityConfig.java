package swm.backstage.movis.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;
import swm.backstage.movis.domain.auth.filter.JwtAccessDeniedHandler;
import swm.backstage.movis.domain.auth.filter.JwtAuthenticationEntryPoint;
import swm.backstage.movis.domain.auth.filter.JwtExceptionFilter;
import swm.backstage.movis.domain.auth.filter.JwtAuthenticationFilter;
import swm.backstage.movis.domain.auth.service.AuthTokenService;
import swm.backstage.movis.domain.auth.utils.JwtUtil;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final AuthTokenService authTokenService;
    private final CorsConfigurationSource corsConfigurationSource;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors((cors) -> cors
                .configurationSource(corsConfigurationSource)
        );

        http.csrf(AbstractHttpConfigurer::disable);

        http.formLogin(AbstractHttpConfigurer::disable);

        http.httpBasic(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers(
                        //swagger
                        "/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**",
                        //auth
                        "/api/*/auth/test/register","/api/*/auth/test/confirmIdentifier","/api/*/auth/test/login",
                        "/api/*/auth/register", "/api/*/auth/confirmIdentifier", "/api/*/auth/login", "/api/*/auth/reissue", "/api/*/auth/publicKey",
                        //club
                        "/api/v1/clubs/forAlert",
                        //event
                        "/api/v1/fees",
                        "/api/v1/events", "/api/v1/events/{eventId}", "/api/v1/events/funding/**",
                        "/api/v1/eventMembers",
                        "/api/v1/eventBill", "/api/v1/eventBill/{eventBillId}",
                        "/api/v1/url-generate",
                        "/api/v1/transactionHistories/fromEvent", "/api/v1/transactionHistories/fromClub", "/api/v1/transactionHistories/unClassification/**"
                        )
                .permitAll()
                .requestMatchers(
                        //auth
                        "/api/*/auth/logout",
                        //club
                        "/api/v1/clubs", "/api/v1/clubs/{clubId}",
                        "/api/v1/members",
                        //test
                        "/api/*/auth/test/manager"
                )
                .hasRole("MANAGER")
                .anyRequest().authenticated()
        );

        http.addFilterBefore(
                new JwtExceptionFilter(handlerExceptionResolver),
                UsernamePasswordAuthenticationFilter.class
        );

        http.addFilterBefore(
                new JwtAuthenticationFilter(jwtUtil, authTokenService),
                UsernamePasswordAuthenticationFilter.class
        );

        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.exceptionHandling(e -> e
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint(handlerExceptionResolver))
                .accessDeniedHandler(new JwtAccessDeniedHandler(handlerExceptionResolver)));

        return http.build();
    }
}
