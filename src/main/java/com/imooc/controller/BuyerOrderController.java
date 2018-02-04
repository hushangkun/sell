package com.imooc.controller;

import com.imooc.converter.OrderForm2OrderDTOConverter;
import com.imooc.dto.OrderDTO;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.form.OrderForm;
import com.imooc.service.BuyerService;
import com.imooc.service.OrderService;
import com.imooc.service.impl.OrderServiceImpl;
import com.imooc.utils.ResultVOUtil;
import com.imooc.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {


    @Autowired
    private OrderService orderService;

    @Autowired
    private BuyerService buyerService;

    //创建订单
    @PostMapping("/create")
    public ResultVO<Map<String, String>> create(@Valid OrderForm orderForm,
           BindingResult bindingResult){ //怎么接收参数呢？ 5个参数，太多了，可以写个对象来进行接收

        if(bindingResult.hasErrors()){
            log.error("【创建订单】 参数不正确， orderForm={}", orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }

        OrderDTO orderDTO = OrderForm2OrderDTOConverter.converter(orderForm);
        if(CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            log.error("【创建订单】购物车不能为空");
            throw new SellException(ResultEnum.CART_EMPTY);
        }

        OrderDTO createResult = orderService.create(orderDTO);

        Map<String, String> map = new HashMap<>();
        map.put("orderId",createResult.getOrderId());

        return ResultVOUtil.success(map);
    }


    //订单列表
    @GetMapping("/list")
    public ResultVO<List<OrderDTO>> list(@RequestParam("openid") String openid,
                                         @RequestParam(value="page",defaultValue = "0") Integer page,
                                         @RequestParam(value="size",defaultValue = "10") Integer size){
        if(StringUtils.isEmpty(openid)){
            log.error("【查询订单列表】 opendid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        PageRequest pageRequest = new PageRequest(page,size);
        Page<OrderDTO> orderDTOPage = orderService.findList(openid, pageRequest);

        /* 问题 1，返回的createTime和updateTime是毫秒类型，文档要求的是秒---怎么处理？
        把Date类型转成long   ---写一个类继承JsonSerializer<Date>  然后再需要的字段上加注解
        @JsonSerialize(using = Date2LongSerializer.class)  //加这个注解，返回时，毫秒会变成秒*/

        /*问题2: 如果我们要求，返回的orderDetailList为null，则该字段不返回， 怎么处理？
         *  可以在orderDTO这个类上加一个注解@JsonInclude(JsonInclude.include.not_null)
         *  如果data为null呢？---- 可以在resultVO上面加该注解，则data为null时，data不返回
         *  对象有很多，怎么才能实现公用呢？----配置文件中配置
         *  spring:
         *     jackson:
         *         default-property-inclusion: non_null
         * 问题3: 如果要求必须返回，而且不能为null呢？-------给对象中的属性初始值
         * */

        return ResultVOUtil.success(orderDTOPage.getContent());


    }

    //订单详情
    @GetMapping("/detail")
    public ResultVO<OrderDTO> detail(@RequestParam("openid") String openid,
                                     @RequestParam("orderId") String orderId){
        if(StringUtils.isEmpty(openid)){
            log.error("【查询订单列表】 opendid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        //TODO  不安全的做法， 待改进
        //OrderDTO orderDTO = orderService.findOne(orderId);
//        if(!orderDTO.getBuyerOpenid().equals(openid)){
//            抛异常
//            但是这样在controller中写这些逻辑又太繁琐，怎么办？-----把所有的逻辑都放到service中做
//            新建一个BuyerService接口
//        }

        OrderDTO orderDTO = buyerService.findOrderOne(openid, orderId);
        return ResultVOUtil.success(orderDTO);

    }

    //取消订单
    @PostMapping("/cancel")
    public ResultVO cancel(@RequestParam("openid") String openid,
                           @RequestParam("orderId") String orderId){
        if(StringUtils.isEmpty(openid)){
            log.error("【查询订单列表】 opendid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        //TODO  不安全的做法， 待改进
       /* OrderDTO orderDTO = orderService.findOne(orderId);
        orderService.cancel(orderDTO);*/

        buyerService.cancelOrder(openid, orderId);
        return ResultVOUtil.success();
    }

}
