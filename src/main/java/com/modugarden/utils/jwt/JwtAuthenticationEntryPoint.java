package com.modugarden.utils.jwt;

import com.modugarden.common.error.exception.custom.BusinessException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.modugarden.common.error.enums.ErrorMessage.UNAUTHORIZED_USER;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
        throw new BusinessException(UNAUTHORIZED_USER);
        //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증되지 않은 유저입니다.");
    }


}