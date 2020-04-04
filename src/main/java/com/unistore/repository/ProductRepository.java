package com.unistore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import com.unistore.entity.ProductEntity;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<ProductEntity, Long> {

  Page<ProductEntity> findBySellerId(String sellerId, Pageable pageable);

}
