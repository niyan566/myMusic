package com.example.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.example.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Value("$jwt.secret")
    private String secret;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取 Authorization 头
        String header = request.getHeader("Authorization");
        if (StrUtil.isBlank(header) || !header.startsWith("Bearer ")) {
            // 如果没有 token 或格式不对，返回 401
            log.info("没有token");
            throw new CustomException("401","令牌验证不通过");
        }

        // 2. 去掉 Bearer 前缀
        String token = header.substring(7);

        try {
            // 3. 验证 token 签名和有效性
            boolean valid = JWTUtil.verify(token, secret.getBytes());
            if (!valid) {
                log.info("token验证不通过");
                throw new CustomException("401","令牌验证不通过");
            }

            // 4. 如果需要解析载荷
            JWT jwt = JWTUtil.parseToken(token);
            Object userId = jwt.getPayload("id"); // 取某个字段
            String userIdString=userId.toString();
            log.info(userIdString);
            request.setAttribute("userId", userId);

        } catch (Exception e) {
            log.info("错误");
            throw new CustomException("401","令牌验证不通过");
        }

        return true; // 放行
    }
}
