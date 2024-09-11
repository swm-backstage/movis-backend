package swm.backstage.movis.global.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
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
import swm.backstage.movis.domain.auth.filter.*;
import swm.backstage.movis.domain.auth.service.AuthTokenService;
import swm.backstage.movis.domain.auth.utils.JwtUtil;
import swm.backstage.movis.domain.club_user.service.ClubUserService;
import swm.backstage.movis.domain.event.service.EventService;
import swm.backstage.movis.domain.member.service.MemberService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final AuthTokenService authTokenService;
    private final EventService eventService;
    private final ClubUserService clubUserService;
    private final MemberService memberService;
    private final CorsConfigurationSource corsConfigurationSource;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public SecurityConfig(JwtUtil jwtUtil,
                          AuthTokenService authTokenService,
                          EventService eventService,
                          ClubUserService clubUserService,
                          MemberService memberService,
                          @Qualifier("corsConfigurationSource") CorsConfigurationSource corsConfigurationSource,
                          @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtUtil = jwtUtil;
        this.authTokenService = authTokenService;
        this.eventService = eventService;
        this.clubUserService = clubUserService;
        this.memberService = memberService;
        this.corsConfigurationSource = corsConfigurationSource;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    /**
     * 상위 계층의 ROLE은 하위 계층의 모든 권한을 가진다.
     * */
    @Bean
    public static RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy("""
                ROLE_MANAGER > ROLE_EXECUTIVE
                ROLE_EXECUTIVE > ROLE_MEMBER
                ROLE_MEMBER > ROLE_AUTHENTICATED
                """);
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
                //permitAll
                .requestMatchers(
                        //swagger
                        "/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**",
                        //auth
                        "/api/*/auth/test/register","/api/*/auth/test/confirmIdentifier","/api/*/auth/test/login",
                        "/api/*/auth/register", "/api/*/auth/confirmIdentifier", "/api/*/auth/login", "/api/*/auth/reissue", "/api/*/auth/publicKey")
                .permitAll()
                .requestMatchers(HttpMethod.POST,
                        //club
                        "/api/v1/clubs")
                .permitAll()

                //MEMBER
                .requestMatchers(HttpMethod.GET,
                        //member
                        "/api/v1/members",
                        //fee
                        "/api/v1/fees", "/api/v1/fees/*",
                        //event
                        "/api/v1/events", "/api/v1/events/*", "/api/v1/events/funding",
                        //event_member
                        "/api/v1/eventMembers",
                        //event_bill
                        "/api/v1/eventBill", "/api/v1/eventBill/*",
                        //club
                        "/api/v1/clubs", "/api/v1/clubs/*", "/api/v1/clubs/forAlert",
                        //transaction_history
                        "/api/v1/transactionHistories/unClassification/**", "/api/v1/transactionHistories/fromEvent", "/api/v1/transactionHistories/fromClub")
                .hasRole("MEMBER")

                //EXECUTIVE
                .requestMatchers(HttpMethod.POST,
                        //member
                        "/api/v1/members",
                        //event
                        "/api/v1/events", "/api/v1/events/gatherFee",
                        //event-member
                        "/api/v1/eventMembers"
                        )
                .hasRole("EXECUTIVE")
                .requestMatchers(HttpMethod.PATCH)
                .hasRole("EXECUTIVE")
                .requestMatchers(HttpMethod.DELETE)
                .hasRole("EXECUTIVE")

                //MANAGER
                .requestMatchers(HttpMethod.GET,
                        //aws
                        "/api/v1/url-generate",
                        //test
                        "/api/v1/auth/test/manager")
                .hasRole("MANAGER")
                .requestMatchers(HttpMethod.POST,
                        //fee
                        "/api/v1/fees", "/api/v1/fees/input", "/api/v1/fees/explanation",
                        //event-bill
                        "/api/v1/eventBill", "/api/v1/eventBill/input", "/api/v1/eventBill/explanation",
                        //alert
                        "/api/v1/alerts"
                        )
                .hasRole("MANAGER")
                .requestMatchers(HttpMethod.PATCH,
                        //fee
                        "/api/v1/fees",
                        //event-bill
                        "/api/v1/eventBill/*")
                .hasRole("MANAGER")
                .requestMatchers(HttpMethod.DELETE)
                .hasRole("MANAGER")
                .anyRequest().denyAll()
        );

        http.addFilterBefore(
                new JwtExceptionFilter(handlerExceptionResolver),
                UsernamePasswordAuthenticationFilter.class
        );

        http.addFilterBefore(
                new JwtAuthenticationFilter(jwtUtil, authTokenService),
                UsernamePasswordAuthenticationFilter.class
        );

        http.addFilterBefore(
                new AuthorityFilter(eventService, clubUserService, memberService),
                UsernamePasswordAuthenticationFilter.class
        )

        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.exceptionHandling(e -> e
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint(handlerExceptionResolver))
                .accessDeniedHandler(new JwtAccessDeniedHandler(handlerExceptionResolver)));

        return http.build();
    }
}
