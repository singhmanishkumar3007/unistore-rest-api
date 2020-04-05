package com.unistore.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.AsyncRestTemplate;
import com.unistore.domain.PaginatedResult;
import com.unistore.domain.ProductPriceDetails;
import com.unistore.entity.PriceEntity;
import com.unistore.entity.ProductEntity;
import com.unistore.exception.UnistoreErrorCode;
import com.unistore.exception.UnistoreException;
import com.unistore.service.ProductPriceAgrregatorService;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("deprecation")
@Service
@Slf4j
public class ProductPriceAggregatorServiceImpl implements ProductPriceAgrregatorService {

  @Autowired
  private AsyncRestTemplate asyncRestTemplate;

  @Override
  public List<ProductPriceDetails> getDetails(Long productId) throws UnistoreException {

    List<ProductPriceDetails> productPriceDetails;
    try {
      productPriceDetails = Collections.singletonList(callEndpoints());
    } catch (Exception e) {
      LOGGER.error("error while getting async call", e);
      throw new UnistoreException(HttpStatus.INTERNAL_SERVER_ERROR, UnistoreErrorCode.SC500,
          "error while getting async call", e);
    }
    return productPriceDetails;
  }



  public ProductPriceDetails callEndpoints(String... args) throws Exception {
    long start = System.currentTimeMillis();

    Future<ResponseEntity<ProductEntity>> future1 =
        asyncRestTemplate.exchange("http://localhost:8080/unistore/api/product/id/1",
            HttpMethod.GET, null, new ParameterizedTypeReference<ProductEntity>() {});
    Future<ResponseEntity<PaginatedResult<PriceEntity>>> future2 = asyncRestTemplate.exchange(
        "http://localhost:8080/unistore/api/price/product/1", HttpMethod.GET, null,
        new ParameterizedTypeReference<PaginatedResult<PriceEntity>>() {});

    while (!(future1.isDone() && future2.isDone())) {
      Thread.sleep(10);
    }

    LOGGER.info("Elapsed time: " + (System.currentTimeMillis() - start));
    System.out.println("future 1 : " + future1.get());
    System.out.println("future 2 : " + future2.get());

    return ProductPriceDetails.builder().productDetails(future1.get().getBody())
        .priceDetails(future2.get().getBody().getResult().get(0)).build();
  }



}
