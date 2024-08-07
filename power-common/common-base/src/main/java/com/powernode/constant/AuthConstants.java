package com.powernode.constant;

/**
 * 认证授权常量类
 */
public interface AuthConstants {

    /**
     * 携带token请求头中的key
     */
    String AUTHORIZATION = "Authorization";

    /**
     * token值的前缀
     */
    String BEARER = "bearer ";

    /**
     * redis中存放token值的前缀
     */
    String LOGIN_TOKEN_PREFIX = "login_token:";
}
