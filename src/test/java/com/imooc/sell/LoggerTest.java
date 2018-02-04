package com.imooc.sell;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class LoggerTest {

    //private final Logger logger = LoggerFactory.getLogger(LoggerTest.class);

    @Test
    public void test1(){
        /*
           使用logger定义时， private final Logger logger = LoggerFactory.getLogger(LoggerTest.class);
        logger.debug("debug...");
        logger.info("info...");
        logger.error("error...");*/

         /**
          * 使用@Slf4j注解时    效果一模一样
          * */
         String name = "imooc";
         String password = "123456";

        log.debug("debug...");
        log.info("name: {}, password: {}", name, password);  //输出变量的格式
        log.error("error...");

    }

}

