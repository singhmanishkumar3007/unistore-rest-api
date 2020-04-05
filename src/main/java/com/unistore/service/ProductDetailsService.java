package com.unistore.service;

import java.util.List;
import java.util.Map;
import com.unistore.domain.PaginatedResult;
import com.unistore.domain.ProductDomain;
import com.unistore.entity.ProductEntity;
import com.unistore.exception.UnistoreException;

public interface ProductDetailsService {

  ProductEntity addProduct(ProductDomain ProductDetails) throws UnistoreException;

  Map<Long, ProductEntity> addMultipleProducts(List<ProductDomain> ProductDetails)
      throws UnistoreException;

  PaginatedResult<ProductEntity> findProducts(Integer page, Integer perPage)
      throws UnistoreException;

  PaginatedResult<ProductEntity> findProductBySellerId(String sellerId, Integer page,
      Integer perPage) throws UnistoreException;

  ProductEntity findProductById(Long id) throws UnistoreException;

}
