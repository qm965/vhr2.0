package org.javaboy.vhr.framework.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.javaboy.vhr.framework.entity.Hr;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import org.springframework.security.authentication.BadCredentialsException;

/**
 * @author：江南一点雨
 * @site：http://www.javaboy.org
 * @微信公众号：江南一点雨
 * @github：https://github.com/lenve
 * @gitee：https://gitee.com/lenve
 */
public class JsonFilter extends UsernamePasswordAuthenticationFilter {
    private final ObjectMapper objectMapper;

    public JsonFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String contentType = request.getContentType();
        if (contentType != null && MediaType.APPLICATION_JSON.isCompatibleWith(MediaType.parseMediaType(contentType))) {
            //认为前端传来的是 JSON 格式的参数
            try {
                //通过 IO 流的形式去解析请求体中的参数
                Hr hr = objectMapper.readValue(request.getInputStream(), Hr.class);
                Object expected = request.getSession().getAttribute("verify_code");
                if (expected == null || hr.getVerifyCode() == null || !expected.toString().equalsIgnoreCase(hr.getVerifyCode().trim())) {
                    throw new BadCredentialsException("验证码错误或已过期");
                }
                request.getSession().removeAttribute("verify_code");
                String username = hr.getUsername();
                username = (username != null) ? username.trim() : "";
                String password = hr.getPassword();
                password = (password != null) ? password : "";
                UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username,
                        password);
                // Allow subclasses to set the "details" property
                setDetails(request, authRequest);
                //获取认证管理器去认证
                return this.getAuthenticationManager().authenticate(authRequest);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            //认为前端传来的是 key-value 格式的参数
            return super.attemptAuthentication(request, response);
        }
    }
}
