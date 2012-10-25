package org.motechproject.whp.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class APIKeyInterceptor extends HandlerInterceptorAdapter {

    private String apiKey;

    public APIKeyInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if(apiKey.equals(request.getHeader("api-key"))){
            return true;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }

}
