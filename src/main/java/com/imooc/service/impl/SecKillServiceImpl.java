package com.imooc.service.impl;

import com.imooc.exception.SellException;
import com.imooc.service.RedisLock;
import com.imooc.service.SecKillService;
import com.imooc.utils.KeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class SecKillServiceImpl implements SecKillService{

    private static final int TIMEOUT = 10 * 1000;  //超时时间10s

    @Autowired
    private RedisLock  redisLock;

    /**
     * 国庆活动，皮蛋粥特价，限量100000份
     * */
    static Map<String, Integer> products;
    static Map<String, Integer> stock;
    static Map<String, String> orders;
    {
        /**
         * 模拟多个表，商品信息表，库存表，秒杀成功订单表
         * */
        products = new HashMap<>();
        stock = new HashMap<>();
        orders = new HashMap<>();
        products.put("123456", 100000);
        stock.put("123456", 100000);
    }

    private String queryMap(String productId){
        return "国庆活动，皮蛋粥特价，限量份"
                + products.get(productId)
                +" 还剩: " + stock.get(productId) + " 份"
                + " 该商品乘成功下单用户数目:"
                + orders.size() + " 人";
    }

    @Override
    public String querySecKillProductInfo(String productId){
        return this.queryMap(productId);
    }

    @Override
    public void orderProductMockDiffUser(String productId){

        //加锁------------如果加锁了，但是下面一段代码出现了异常，导致没有解锁，下一个请求进来的时候，加锁就加不上去，就会导致死锁
        long time = System.currentTimeMillis() + TIMEOUT;
        if(redisLock.lock(productId, String.valueOf(time))){
            throw new SellException(101, "哎呀， 人也太多了， 换个姿势再试试---");
        }

        //1. 查询该商品库存，为0则活动结束
        int stockNum = stock.get(productId);
        if(stockNum == 0){
            throw new SellException(100, "活动结束");
        }else{
            //2. 下单 （模拟不同用户openid不同）
            orders.put(KeyUtils.getUniqueKey(), productId);
            //3. 减库存
            stockNum = stockNum - 1;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stock.put(productId, stockNum);
        }

        //解锁
        redisLock.unlock(productId, String.valueOf(time));
    }

    /**总结：
     *      1. 加上synchronized关键字， 是一种解决办法  public synchronized void orderProductMockDiffUser(String productId)
     *      无法做到细粒度控制，商品不一样（productId），任何商品都会慢，只适合单点的情况
     *
     *      2. redis的分布式琐   命令1.setnx key value      命令2.getset key value
     *          1. 支持分布式
     *          2. 可以更细粒度的控制
     *          3. 多台机器上多个进程对一个数据进行操作的互斥， redis是单线程的
     *
     * */


    /**redis缓存  -------- 命中、失效、更新
     *
     *1. 加启动类上加一个注解@EnableCaching
     *
     *
     * */


}
