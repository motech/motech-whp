package org.motechproject.whp.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

@Component
public class PropertyInjectorInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    @Qualifier("whpProperties")
    private Properties whpProperties;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null)
            modelAndView.getModel().put("applicationVersion", whpProperties.get("application.version"));
        super.postHandle(request, response, handler, modelAndView);
    }
}
