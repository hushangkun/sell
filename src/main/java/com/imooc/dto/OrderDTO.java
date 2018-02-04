package com.imooc.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.imooc.dataobject.OrderDetail;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayStatusEnum;
import com.imooc.utils.EnumUtil;
import com.imooc.utils.serializer.Date2LongSerializer;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderDTO {

    //订单id
    private String orderId;

    //买家名字
    private String buyerName;

    //买家手机号
    private String buyerPhone;

    //买家地址
    private String buyerAddress;

    //买家微信openid
    private String buyerOpenid;

    //订单总金额
    private BigDecimal orderAmount;

    //订单状态 默认为新下单 0  切忌在代码中写常量，可以新建一个枚举
    private Integer orderStatus;

    //支付状态，默认为0未支付
    private Integer payStatus;

    //创建时间
    @JsonSerialize(using = Date2LongSerializer.class)  //加这个注解，返回时，毫秒会变成秒
    private Date createTime;

    //更新时间
    @JsonSerialize(using = Date2LongSerializer.class)  //加这个注解，返回时，毫秒会变成秒
    private Date updateTime;

    //数据库里面是没有这个字段，那么就会报错，怎么办？---为了报错，加个注解@Transient,就不会和数据库中对应起来了
    //但是这样不太好，有的是和数据库对应的，有的又是给controller用的，所以可以新建一个类dto  专门在各个层传输用的
    @Transient
    private List<OrderDetail> orderDetailList;

    @JsonIgnore  //如果有人把这个对象返回了，要忽略这俩个字段，用注解@JsonIgnore
    public OrderStatusEnum getOrderStatusEnum(){
        //return OrderStatusEnum.getOrderStatusEnum(orderStatus);
        return EnumUtil.getByCode(orderStatus, OrderStatusEnum.class);

    }

    @JsonIgnore //如果有人把这个对象返回了，要忽略这俩个字段，用注解@JsonIgnore
    public PayStatusEnum getPayStatusEnum(){
        //return PayStatusEnum.getPayStatusEnum(payStatus);
        return EnumUtil.getByCode(payStatus, PayStatusEnum.class);
    }

    //枚举中都写了这个一个方法，那么能不能抽象起来呢？


}
