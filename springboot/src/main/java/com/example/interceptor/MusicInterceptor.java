package com.example.interceptor;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class MusicInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader("Authorization");
        if (header!=null&&header.startsWith("Bearer")){
            try {
                JWT jwt = JWTUtil.parseToken(header.substring(7));
                Integer id = Integer.valueOf(jwt.getPayload("id").toString());
                request.setAttribute("userId",id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }
}
