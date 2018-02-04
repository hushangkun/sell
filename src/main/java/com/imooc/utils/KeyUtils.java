package com.imooc.utils;

import java.util.Random;

public class KeyUtils {

    /**
     * 生成唯一的主键
     * 格式：时间+随机数
     * @return
     */
    public static synchronized String getUniqueKey(){
        Random random = new Random();
        //生成6位随机数     生成2位随机数  random.nextInt(90) + 10;
        Integer number = random.nextInt(900000) + 100000;
        //在多线程并发的时候可能还是会重复的， 怎么办？----需要加一个synchronized的关键字
        return System.currentTimeMillis() + String.valueOf(number);
    }
}
