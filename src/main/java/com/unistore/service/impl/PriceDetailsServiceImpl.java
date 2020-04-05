package com.unistore.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.unistore.domain.PaginatedResult;
import com.unistore.entity.PriceEntity;
import com.unistore.exception.UnistoreErrorCode;
import com.unistore.exception.UnistoreException;
import com.unistore.repository.PriceRepository;
import com.unistore.service.PriceDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class PriceDetailsServiceImpl implements PriceDetailsService {

  private final PriceRepository priceRepository;



  @Override
  public PriceEntity addPrice(PriceEntity priceEntity) throws UnistoreException {

    try {
      PriceEntity priceAdded = priceRepository.save(priceEntity);
      LOGGER.info("price added successfully for product id :{}", priceEntity.getProductId());
      return priceAdded;
    } catch (Exception e) {
      LOGGER.error("Exception while saving new price :", e);
      throw new UnistoreException(HttpStatus.INTERNAL_SERVER_ERROR, UnistoreErrorCode.SC500,
          "Exception while saving new price :" + e.getMessage(), e);
    }
  }


  @SuppressWarnings("deprecation")
  @Override
  public PaginatedResult<PriceEntity> findPriceByProductId(Long productId, Integer page,
      Integer perPage) throws UnistoreException {
    Pageable pageable = new PageRequest(page, perPage);
    Page<PriceEntity> priceEntitiesInPage;
    try {
      priceEntitiesInPage = priceRepository.findByProductId(productId, pageable);
    } catch (Exception e) {
      LOGGER.error("Exception while finding price of product id {} :", productId, e);
      throw new UnistoreException(HttpStatus.INTERNAL_SERVER_ERROR, UnistoreErrorCode.SC500,
          "Exception while finding price of product id " + productId + " :" + e.getMessage(), e);
    }

    Long totalNumberOfPages = priceEntitiesInPage.getTotalElements();
    List<PriceEntity> priceList = priceEntitiesInPage.getContent();
    PaginatedResult<PriceEntity> paginatedPrice =
        new PaginatedResult<>(totalNumberOfPages, priceList);
    return paginatedPrice;
  }


}
