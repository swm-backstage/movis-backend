package swm.backstage.movis.domain.auth.filter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {


    private final HandlerExceptionResolver resolver;

    public CustomAccessDeniedHandler(@Qualifier("handlerExceptionResolver")HandlerExceptionResolver resolver) {

        this.resolver = resolver;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException, IOException {

        resolver.resolveException(
                request,
                response,
                null,
                new BaseException("요청에 대한 사용자의 권한이 없습니다.", ErrorCode.UNAUTHORIZED_PERMISSION)
        );
    }

}
