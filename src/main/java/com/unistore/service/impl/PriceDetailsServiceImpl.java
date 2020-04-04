package com.unistore.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.unistore.domain.PaginatedResult;
import com.unistore.entity.PriceEntity;
import com.unistore.repository.PriceRepository;
import com.unistore.service.PriceDetailsService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PriceDetailsServiceImpl implements PriceDetailsService {

  private final PriceRepository priceRepository;



  @Override
  public PriceEntity addPrice(PriceEntity priceEntity) {

    PriceEntity priceAdded = priceRepository.save(priceEntity);
    return priceAdded;
  }


  @SuppressWarnings("deprecation")
  @Override
  public PaginatedResult<PriceEntity> findPriceByProductId(Long productId, Integer page,
      Integer perPage) {
    Pageable pageable = new PageRequest(page, perPage);
    Page<PriceEntity> priceEntitiesInPage = priceRepository.findByProductId(productId, pageable);
    Long totalNumberOfPages = priceEntitiesInPage.getTotalElements();
    List<PriceEntity> priceList = priceEntitiesInPage.getContent();
    PaginatedResult<PriceEntity> paginatedPrice =
        new PaginatedResult<>(totalNumberOfPages, priceList);
    return paginatedPrice;
  }


}
