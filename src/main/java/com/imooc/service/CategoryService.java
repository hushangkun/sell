package com.imooc.service;


import com.imooc.dataobject.ProductCategory;

import java.util.List;
public interface CategoryService {

    ProductCategory findOne(Integer categoryId);

    List<ProductCategory> findAll();

    List<ProductCategory> findByCategoryTypeIn(List<String> categoryTypeList);

    ProductCategory save(ProductCategory productCategory);
}
