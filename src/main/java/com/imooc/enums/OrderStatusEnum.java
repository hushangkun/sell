package com.imooc.enums;

import lombok.Getter;

/**
 *订单状态
 */
@Getter
public enum OrderStatusEnum implements CodeEnum{

    NEW(0, "新订单"),
    FINISHED(1,"完结"),
    CANCEL(2,"已取消"),
    ;
    private Integer code;

    private String message;

    OrderStatusEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    //和OrderDTO中的方法对应起来，为了在list.ftl中通用的显示出信息，
//    public static OrderStatusEnum getOrderStatusEnum(Integer code){
//        for(OrderStatusEnum orderStatusEnum : OrderStatusEnum.values()){
//            if(orderStatusEnum.getCode().equals(code)){
//                return orderStatusEnum;
//            }
//        }
//        return null;
//    }
}
