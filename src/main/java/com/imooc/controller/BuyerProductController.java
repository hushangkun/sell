package com.imooc.controller;

import com.imooc.dataobject.ProductCategory;
import com.imooc.dataobject.ProductInfo;
import com.imooc.service.impl.CategoryServiceImpl;
import com.imooc.service.impl.ProductServiceImpl;
import com.imooc.utils.ResultVOUtil;
import com.imooc.vo.ProductInfoVO;
import com.imooc.vo.ProductVO;
import com.imooc.vo.ResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 买家商品
 */
@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping("/list")
//    @Cacheable(cacheNames="product", key="123")  //存储的时候会进行序列化
    //key可以动态写   ske表达式   如果#sellerId.length() > 3结果成立，那么就会缓存    如果要依据resultVO中的code来判断，code为0时才缓存，该怎么弄？ 使用unless属性(这个属性用的比较多，我们不可能把错误的结果也缓存起来)
    @Cacheable(cacheNames =  "product", key = "#sellerId", condition = "#sellerId.length() > 3", unless = "#result.getCode() != 0")
    public ResultVO list(String sellerId){
        ResultVO resultVO = new ResultVO();
        //查询所有上架的商品信息----取出所有的categoryType， 放入集合中去查所有的类目信息
        List<ProductInfo> productInfoList =  productService.findUpAll();
        List<String> categoryTypeList = new ArrayList<>();
        //1. 传统方法
        for(ProductInfo productInfo : productInfoList){
            categoryTypeList.add(productInfo.getCategoryType());
        }
        //2. java8中的lomda表达式---?
        //TODO
//        List<ProductCategory> categoryTypeList = productInfoList.stream()
//                .map(e -> e.getCategoryType())
//                .collect(Collectors.toList());

        List<ProductCategory> productCategoryList = categoryService.findByCategoryTypeIn(categoryTypeList);

        List<ProductVO> productVOList = new ArrayList<>();
        //忌讳： 不要把数据库的查询放到for循环中
        for(ProductCategory productCategory : productCategoryList){
            ProductVO productVO = new ProductVO();
            productVO.setCategoryName(productCategory.getCategoryName());
            productVO.setCategoryType(productCategory.getCategoryId());

            List<ProductInfoVO> productInfoList2 = new ArrayList<>();
            for(ProductInfo productInfo : productInfoList){
                if(productCategory.getCategoryType().equals(productInfo.getCategoryType())){
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo, productInfoVO);
                    productInfoList2.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOlist(productInfoList2);
            productVOList.add(productVO);
        }
        //每一个resultVO都要这样写，有点多余，可以封装一下
        /*resultVO.setData(productVOList);
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;*/
        return ResultVOUtil.success(productVOList);

    }
}
