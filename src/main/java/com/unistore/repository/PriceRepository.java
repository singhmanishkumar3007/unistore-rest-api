package com.unistore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import com.unistore.entity.PriceEntity;

@Repository
public interface PriceRepository extends PagingAndSortingRepository<PriceEntity, Integer> {

  Page<PriceEntity> findByProductId(Long productId, Pageable pageable);

}
