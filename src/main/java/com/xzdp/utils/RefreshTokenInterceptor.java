package com.xzdp.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.xzdp.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.xzdp.utils.RedisConstants.LOGIN_CODE_KEY;
import static com.xzdp.utils.RedisConstants.LOGIN_USER_TTL;

public class RefreshTokenInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RefreshTokenInterceptor.class);
    private StringRedisTemplate stringRedisTemplate;
    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("authorization");
        log.debug(token);
        if (StrUtil.isBlank(token)) {
            return true;
        }
        log.debug("Here");
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(LOGIN_CODE_KEY + token);

        if (userMap.isEmpty()) {
            return true;
        }
        log.debug("Here userMap");
        UserDTO userDTO  = BeanUtil.fillBeanWithMap(userMap, new UserDTO(), false);

        UserHolder.saveUser(userDTO);

        stringRedisTemplate.expire(LOGIN_CODE_KEY + token, LOGIN_USER_TTL, TimeUnit.MINUTES);
        return true;
    }
}
