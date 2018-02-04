package com.imooc.repository;

import com.imooc.dataobject.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {

    @Autowired
    private ProductCategoryRepository repository;

    @Test
    public void findOneTest(){
        ProductCategory productCategory = this.repository.findOne(1);
        System.out.println(productCategory.toString());
    }

    @Test
    @Transactional   //该注解和Service中的注解不一样，在test中会完全回滚，不管有没有发生异常
    public void saveTest(){
        ProductCategory productCategory = new ProductCategory("女生最爱","3");
        ProductCategory result = repository.save(productCategory);
        Assert.assertNotNull(result);
        //两个方法是一样的
        //Assert.assertNotEquals(null,result);
    }

    @Test
    public void  findByCategoryTypeInTest(){
        List<String> list = Arrays.asList("2","3","4");
        //注意，这个地方需要空参构造方法
        List<ProductCategory> result = repository.findByCategoryTypeIn(list);
        Assert.assertNotEquals(0,result.size());
    }



}