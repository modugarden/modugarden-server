package com.modugarden.utils.jwt;

import com.modugarden.common.error.enums.ErrorMessage;
import com.modugarden.common.error.exception.custom.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 서블릿 필터에서 오류 발생시 처리할 exception Handler
 * -> 스프링부트에서 exception 처리시 컨트롤러에서 발생한 exception만 핸들링 가능
 */
@Slf4j
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        } catch (BusinessException ex){
            log.error("exception exception handler filter");
            setErrorResponse(response, ex.getErrorMessage());
        }
    }

    //한글 출력을 위해 getWriter() 사용
    private void setErrorResponse(HttpServletResponse response, ErrorMessage errorMessage){
        response.setStatus(errorMessage.getCode());
        response.setContentType("application/json;charset=UTF-8");
        try{
            JSONObject responseJson = new JSONObject();
            responseJson.put("code", errorMessage.getCode());
            responseJson.put("message", errorMessage.getMessage());
            responseJson.put("isSuccess", "false");
            response.getWriter().print(responseJson);
        }catch (JSONException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
