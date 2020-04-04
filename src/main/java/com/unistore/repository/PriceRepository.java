package com.unistore.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.unistore.entity.PriceEntity;

public interface PriceRepository extends PagingAndSortingRepository<PriceEntity, Integer> {



}
