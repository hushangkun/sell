package com.imooc.constant;

/**
 * redis常量
 */
public interface RedisConstant {

    String TOKEN_PREFIX = "token_%s";  //设置一个前缀

    Integer EXPIRE = 7200; //2小时
}
