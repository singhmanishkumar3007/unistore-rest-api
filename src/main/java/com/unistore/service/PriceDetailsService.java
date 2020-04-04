package com.unistore.service;

import com.unistore.domain.PaginatedResult;
import com.unistore.entity.PriceEntity;

public interface PriceDetailsService {

  PriceEntity addPrice(PriceEntity ProductDetails);

  PaginatedResult<PriceEntity> findPriceByProductId(Long productId, Integer page, Integer perPage);


}
