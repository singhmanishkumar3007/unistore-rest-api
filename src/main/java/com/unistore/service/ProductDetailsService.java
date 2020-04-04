package com.unistore.service;

import java.util.List;
import java.util.Map;
import com.unistore.domain.PaginatedResult;
import com.unistore.entity.ProductEntity;

public interface ProductDetailsService {

  ProductEntity addProduct(ProductEntity ProductDetails);

  Map<Long, ProductEntity> addMultipleProducts(List<ProductEntity> ProductDetails);

  PaginatedResult<ProductEntity> findProducts(Integer page, Integer perPage);

  PaginatedResult<ProductEntity> findProductBySellerId(String sellerId,Integer page, Integer perPage);

  ProductEntity findProductById(Long id);

}
