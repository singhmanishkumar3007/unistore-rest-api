package com.unistore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.unistore.domain.PaginatedResult;
import com.unistore.domain.ProductPriceDetails;
import com.unistore.entity.PriceEntity;
import com.unistore.entity.ProductEntity;
import com.unistore.exception.UnistoreErrorCode;
import com.unistore.exception.UnistoreException;
import com.unistore.service.ProductPriceAgrregatorService;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("deprecation")
@Service
@Slf4j
public class ProductPriceAggregatorServiceImpl implements ProductPriceAgrregatorService {

  // @Autowired
  // private AsyncRestTemplate asyncRestTemplate;

  @Value("${url.product}")
  private String productURL;

  @Value("${url.price}")
  private String priceURL;


  @Autowired
  private RestTemplate restTemplate;


  // @SuppressWarnings("unchecked")
  @Override
  public Observable<ProductPriceDetails> getProductPriceDetails(Long productId)
      throws UnistoreException {
    LOGGER.debug("Retrieving the product price Details.");
    try {
      return (Observable<ProductPriceDetails>) Observable.zip(
          getProductDetails(productId).subscribeOn(Schedulers.io()),
          getPriceInformation(productId).subscribeOn(Schedulers.io()),
          (productResult, priceResult) -> transformToPrdouctPrice(productResult, priceResult));
      // .observeOn(Schedulers.newThread())
      // .subscribe(result -> System.out.println("product price aggregaation is :" + result));

    } catch (Exception e) {
      LOGGER.error("Exception while fetching product price details :", e);
      throw new UnistoreException(HttpStatus.INTERNAL_SERVER_ERROR, UnistoreErrorCode.SC500,
          "Exception while fetching product price details :" + e.getMessage(), e);
    }
  }



  private ProductPriceDetails transformToPrdouctPrice(ProductEntity productResult,
      PriceEntity priceResult) {

    return ProductPriceDetails.builder().productDetails(productResult).priceDetails(priceResult)
        .build();
  }



  private Observable<ProductEntity> getProductDetails(Long productId) {
    return Observable.<ProductEntity>create(sub -> {
      ProductEntity productEntity =
          restTemplate.getForEntity(productURL + productId, ProductEntity.class).getBody();
      sub.onNext(productEntity);
      sub.onComplete();
    }).doOnNext(p -> LOGGER.info("Product details were received successfully."))
        .doOnError(e -> LOGGER.error("An ERROR occurred while fetching the Product details.", e));
  }

  private Observable<PriceEntity> getPriceInformation(Long productId) {
    return Observable.<PriceEntity>create(sub -> {
      PriceEntity priceEntity = restTemplate
          .exchange(priceURL + productId, HttpMethod.GET, null,
              new ParameterizedTypeReference<PaginatedResult<PriceEntity>>() {})
          .getBody().getResult().stream().findFirst().orElse(PriceEntity.builder().build());
      sub.onNext(priceEntity);
      sub.onComplete();
    }).doOnNext(s -> LOGGER.info("Price information was received successfully."))
        .doOnError(e -> LOGGER.error("An ERROR occurred while fetching Price Information", e));
  }



}
