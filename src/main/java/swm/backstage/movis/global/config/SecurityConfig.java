package swm.backstage.movis.global.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;
import swm.backstage.movis.domain.auth.filter.*;
import swm.backstage.movis.domain.auth.service.AuthTokenService;
import swm.backstage.movis.domain.auth.utils.JwtUtil;
import swm.backstage.movis.domain.club_user.service.ClubUserService;
import swm.backstage.movis.domain.event.service.EventService;
import swm.backstage.movis.domain.member.service.MemberService;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final CorsConfigurationSource corsConfigurationSource;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final AuthTokenService authTokenService;
    private final ClubUserService clubUserService;
    private final MemberService memberService;
    private final EventService eventService;

    public SecurityConfig(JwtUtil jwtUtil,
                          @Qualifier("corsConfigurationSource") CorsConfigurationSource corsConfigurationSource,
                          @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver,
                          AuthTokenService authTokenService,
                          ClubUserService clubUserService,
                          MemberService memberService,
                          EventService eventService
                          ) {
        this.jwtUtil = jwtUtil;
        this.corsConfigurationSource = corsConfigurationSource;
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.authTokenService = authTokenService;
        this.clubUserService = clubUserService;
        this.memberService = memberService;
        this.eventService = eventService;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public MethodSecurityExpressionHandler expressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator(clubUserService, memberService, eventService));
        return expressionHandler;
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
                        .anyRequest().permitAll()
        );

        http.addFilterBefore(
                new CustomExceptionFilter(handlerExceptionResolver),
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
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint(handlerExceptionResolver))
                .accessDeniedHandler(new CustomAccessDeniedHandler(handlerExceptionResolver)));

        return http.build();
    }
}
