package com.powernode.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.powernode.config.WhiteUrlsConfig;
import com.powernode.constant.AuthConstants;
import com.powernode.constant.BusinessEnum;
import com.powernode.constant.HttpConstants;
import com.powernode.model.Result;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;

/**
 * 全局token校验过滤器
 * 作用：
 *    校验请求的合法性，拦截非法请求。
 * 前提：
 *    后端开发与前端开发约定好token存放的规则。
 *    存放在请求头header中的Authorization bearer token
 */
@Component
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private WhiteUrlsConfig whiteUrlsConfig;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().toString();
        if(whiteUrlsConfig.getAllowUrls().contains(path))
        {
            return chain.filter(exchange);
        }
        //不在白名单中，需要对其身份进行验证
        String authorizationValue = request.getHeaders().getFirst(AuthConstants.AUTHORIZATION);
        if(StringUtils.hasText(authorizationValue))
        {
            String token = authorizationValue.replaceFirst(AuthConstants.BEARER, "");
            if(StringUtils.hasText(token) && stringRedisTemplate.hasKey(AuthConstants.LOGIN_TOKEN_PREFIX+token))
            {
                return chain.filter(exchange);
            }
        }

        // 打印 拦截非法请求日志
        log.error("拦截非法请求，拦截时间:{}，请求api接口:{}",new Date(),path);

        // 拦截非法请求，响应消息
        // 获取响应对象
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().set(HttpConstants.CONTENT_TYPE,HttpConstants.APPLICATION_JSON);

        Result<Object> result = Result.fail(BusinessEnum.UN_AUTHORIZATION);
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] bytes = new byte[0];
        try {
            bytes = objectMapper.writeValueAsBytes(result);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        DataBuffer wrap = response.bufferFactory().wrap(bytes);

        return response.writeWith(Mono.just(wrap));
    }


    @Override
    public int getOrder() {
        return -5;
    }
}
