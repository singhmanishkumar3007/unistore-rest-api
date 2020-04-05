package com.unistore.service;

import com.unistore.domain.PaginatedResult;
import com.unistore.entity.PriceEntity;
import com.unistore.exception.UnistoreException;

public interface PriceDetailsService {

  PriceEntity addPrice(PriceEntity ProductDetails) throws UnistoreException;

  PaginatedResult<PriceEntity> findPriceByProductId(Long productId, Integer page, Integer perPage)
      throws UnistoreException;


}
