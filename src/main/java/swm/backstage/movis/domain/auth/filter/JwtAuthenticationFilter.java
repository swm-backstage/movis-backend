package swm.backstage.movis.domain.auth.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import swm.backstage.movis.domain.auth.AuthToken;
import swm.backstage.movis.domain.auth.dto.AuthenticationPrincipalDetails;
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

    private static final List<String> EXCLUDED_PATHS = List.of(
            // 클라이언트측에서 로그인 상태(만료된 토큰 포함)에서 한번 더 요청을 보낼 경우, 현재 필터에서 만료시간 에러를 발생시키기 때문에 무조건 패스
            "/api/v1/auth/test/login",
            // reissue의 요청헤더에 accessToken이 포함되어 있을 경우, 현재 필터에서 만료시간 에러를 발생시키기 때문에 무조건 패스
            "/api/v1/auth/reissue"
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

        // token이 존재하지 않는다면, 그대로 통과
        String accessTokenWithBearer = request.getHeader(jwtUtil.getACCESS_TOKEN_NAME());
        if(!StringUtils.hasText(accessTokenWithBearer)){
            filterChain.doFilter(request, response);
        }

        // Bearer 포함 검사 및 제거
        String accessToken;
        if (accessTokenWithBearer.startsWith("Bearer ")) {
            accessToken = accessTokenWithBearer.substring(7);
        } else {
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
                new AuthenticationPrincipalDetails(identifier, jwtUtil.getPlatformType(accessToken)),
                null,
                null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
