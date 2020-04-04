package com.unistore.service.impl;

import java.util.Arrays;
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
import com.unistore.exception.StandardError;
import com.unistore.exception.StandardErrorCode;
import com.unistore.exception.StandardException;
import com.unistore.service.ProductPriceAgrregatorService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductPriceAggregatorServiceImpl implements ProductPriceAgrregatorService {

  @Autowired
  private AsyncRestTemplate asyncRestTemplate;

  @Override
  public List<ProductPriceDetails> getDetails(Long productId) throws StandardException {

    List<ProductPriceDetails> productPriceDetails;
    try {
      productPriceDetails = Collections.singletonList(callEndpoints());
    } catch (Exception e) {
      throw new StandardException(HttpStatus.INTERNAL_SERVER_ERROR,
          Arrays.asList(StandardError.builder().message("error while getting async call").build()),
          StandardErrorCode.SC500, e);
    }
    return productPriceDetails;
  }



  @SuppressWarnings({"deprecation"})
  public ProductPriceDetails callEndpoints(String... args) throws Exception {
    // Start the clock
    long start = System.currentTimeMillis();

    // Kick of multiple, asynchronous lookups
    Future<ResponseEntity<PaginatedResult<ProductEntity>>> future1 = asyncRestTemplate.exchange(
        "http://localhost:8080/unistore/api/product/id/1", HttpMethod.GET, null,
        new ParameterizedTypeReference<PaginatedResult<ProductEntity>>() {});
    Future<ResponseEntity<PaginatedResult<PriceEntity>>> future2 = asyncRestTemplate.exchange(
        "http://localhost:8080/unistore/api/price/product/1", HttpMethod.GET, null,
        new ParameterizedTypeReference<PaginatedResult<PriceEntity>>() {});

    // Wait until they are all done
    while (!(future1.isDone() && future2.isDone())) {
      Thread.sleep(10); // 10-millisecond pause between each check
    }

    // Print results, including elapsed time
    LOGGER.info("Elapsed time: " + (System.currentTimeMillis() - start));
    LOGGER.info("future 1 : ", future1.get());
    LOGGER.info("future 2 : ", future2.get());

    return ProductPriceDetails.builder()
        .productDetails((ProductEntity) future1.get().getBody().getResult().get(0))
        .priceDetails((PriceEntity) future2.get().getBody().getResult().get(0)).build();
  }



}
