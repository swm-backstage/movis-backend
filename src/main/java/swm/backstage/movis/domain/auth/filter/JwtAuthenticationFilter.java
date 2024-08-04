package swm.backstage.movis.domain.auth.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import swm.backstage.movis.domain.auth.AuthToken;
import swm.backstage.movis.domain.auth.service.AuthTokenService;
import swm.backstage.movis.domain.auth.utils.JwtUtil;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

import java.io.IOException;
import java.util.List;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AuthTokenService authTokenService;
    private final AntPathMatcher pathMatcher;

    // TODO: HTTP Method 구별을 위해 Map 사용
    private static final List<String> EXCLUDED_PATHS = List.of(
            //auth
            "/api/v1/auth/test/register",
            "/api/v1/auth/test/confirmIdentifier",
            "/api/v1/auth/test/login",
            "/api/v1/auth/register",
            "/api/v1/auth/confirmIdentifier",
            "/api/v1/auth/login",
            "/api/v1/auth/reissue", // reissue 요청에는 accessToken이 포함되어있지 않다.
            "/api/v1/auth/publicKey",
            //모임, 이벤트 조회
            "/api/v1/members",
            "/api/v1/fees",
            "/api/v1/events", "/api/v1/events/{eventId}",
            "/api/v1/eventMembers",
            "/api/v1/eventBill", "/api/v1/eventBill/{eventBillId}",
            "/api/v1/clubs", "/api/v1/clubs/{clubId}",
            "/api/v1/url-generate",
            "/api/v1/transactionHistories/fromEvent", "/api/v1/transactionHistories/fromClub",
            //swagger
            "/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**"
    );

    public JwtAuthenticationFilter(JwtUtil jwtUtil, AuthTokenService authTokenService) {
        
        this.jwtUtil = jwtUtil;
        this.authTokenService = authTokenService;
        this.pathMatcher = new AntPathMatcher();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String requestUri = request.getRequestURI();
        return EXCLUDED_PATHS.stream().anyMatch(path -> pathMatcher.match(path, requestUri));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // token 문자열 형식 검사
        String accessToken = jwtUtil.resolveToken(request.getHeader(jwtUtil.getACCESS_TOKEN_NAME()));
        if (!StringUtils.hasText(accessToken)) {

            throw new BaseException("옳바르지 않은 토큰 형식입니다. ", ErrorCode.INVALID_TOKEN_FORMAT);
        }

        // accessToken 여부 검사
        if (!jwtUtil.getTokenType(accessToken).equals(jwtUtil.getACCESS_TOKEN_NAME())){

            throw new BaseException("유효하지 않은 토큰 입니다. 다시 로그인 해주세요. ", ErrorCode.INVALID_TOKEN);
        }

        // token 유효성 검사
        try {

            jwtUtil.validateToken(accessToken);
        } catch (ExpiredJwtException e) {

            throw new BaseException("토큰이 만료되었습니다. 토큰을 재발급 해주세요. ", ErrorCode.EXPIRED_TOKEN);
        }
        catch (JwtException e) {

            throw new BaseException("유효하지 않은 토큰 입니다. 다시 로그인 해주세요. ", ErrorCode.INVALID_TOKEN);
        }

        // 요청된 accessToken이 server에 있는 accessToken과 일치하는지 검사.
        String identifier = jwtUtil.getIdentifier(accessToken);
        AuthToken savedAuthToken = authTokenService.findByIdentifier(identifier).orElseThrow(() ->
                new BaseException("유효하지 않은 토큰 입니다. 다시 로그인 해주세요. ", ErrorCode.INVALID_TOKEN));
        if (!accessToken.equals(savedAuthToken.getAccessToken())){

            throw new BaseException("유효하지 않은 토큰 입니다. 다시 로그인 해주세요. ", ErrorCode.INVALID_TOKEN);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                identifier,
                null,
                List.of(new SimpleGrantedAuthority(jwtUtil.getRole(accessToken))));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
