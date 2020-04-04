package com.unistore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.unistore.entity.ProductEntity;

public interface ProductRepository extends PagingAndSortingRepository<ProductEntity, Long> {

  Page<ProductEntity> findBySellerId(String sellerId, Pageable pageable);

}
