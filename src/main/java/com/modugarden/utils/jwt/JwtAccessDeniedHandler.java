package com.modugarden.utils.jwt;

import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.common.error.exception.custom.LoginCancelException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.modugarden.common.error.enums.ErrorMessage.FORBIDDEN_USER;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        //필요한 권한이 없이 접근하려 할때 403
        throw new LoginCancelException(FORBIDDEN_USER);
        //response.sendError(HttpServletResponse.SC_FORBIDDEN, "접근 권한이 없습니다.");
    }
}