package com.imooc.dataobject.mapper;

import com.imooc.dataobject.ProductCategory;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface ProductCategoryMapper {

    //mybatis 注解形式

    //第一种，通过map形式写入
    @Insert("insert into product_category(category_name, category_type) values (#{categoryName, jdbcType=VARCHAR}, #{categoryType, jdbcType=INTEGER})")
    int insertByMap(Map<String, Object> map);

    //第二种，通过对象形式写入（用的最多）
    @Insert("insert into product_category(category_name, category_type) values (#{categoryName, jdbcType=VARCHAR}, #{categoryType, jdbcType=INTEGER})")
    int insertByObject(ProductCategory productCategory);

    //根据categoryType查询
    @Select("select * from product_category where category_type = #{categoryType}")
    @Results({  //在mapper中还需要加一个注解@Results(),将数据库中查出来的字段映射成对象中的字段
            @Result(column = "category_id", property = "categoryId"),
            @Result(column = "category_name", property = "categoryName"),
            @Result(column = "category_type", property = "categoryType")
    })
    ProductCategory findByCategoryType(Integer categoryType);

    //根据categoryName查询
    @Select("select * from product_category where category_name = #{categoryName}")
    @Results({  //在mapper中还需要加一个注解@Results(),将数据库中查出来的字段映射成对象中的字段
            @Result(column = "category_id", property = "categoryId"),
            @Result(column = "category_name", property = "categoryName"),
            @Result(column = "category_type", property = "categoryType")
    })
    List<ProductCategory> findByCategoryName(String categoryName);

    //通过categoryType更新  传多个参数时， 需要加注解@Param
    @Update("update product_category set cagegory_name = #{categoryName} where category_type = #{categoryType}")
    int updateByCategoryType(@Param("categoryName") String categoryName,
                             @Param("categoryType") Integer categoryType);


    //通过对象更新
    @Update("update product_category set category_name = #{categoryName} where category_type = #{categoryType}")
    int updateByObject(ProductCategory productCategory);


    //删除
    @Delete("delete from product_category where category_type = #{categoryType}")
    int deleteByCategoryType(Integer categoryType);


    //mybatis xml形式
    ProductCategory selectByCategoryType(Integer categoryType);


    /**
     * JPA和mybatis怎么选？
     * 1. 听领导的
     * 2. 如果领导无所谓，那可以根据自己的喜好
     * 3. 建表用sql， 不用jpa建表
     * 4. 慎用@OneToMany 和 @ManyToOne, 扩展性不好，对以后的分库分表非常困难
     *
     * */

    /**
     * 用压测模拟并发
     * 1. 使用简易工具Apache ab   可以模拟并发  在命令行中输入 ab -n 100 -c 100 http://www.baidu.com/   这条命令表示: -n 100 发出100个请求   -c 100  100的并发   相当于：100个人同时访问
     *
     *                    ab -n 100 -c 100 http://www.baidu.com/           表示： 在60s内会不停的发请求
     *
     * 2. 类似的压测工具还有Jmeter等
     * */



}
