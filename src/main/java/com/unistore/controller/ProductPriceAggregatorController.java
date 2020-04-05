package com.unistore.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.unistore.domain.ProductPriceDetails;
import com.unistore.exception.ErrorDetails;
import com.unistore.service.ProductPriceAgrregatorService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/product-price")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductPriceAggregatorController {

  private final ProductPriceAgrregatorService productPriceAgrregatorService;



  @ApiOperation(httpMethod = "GET", value = "Fetch product-price by product id",
      response = String.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "product-price  details fetched succesfully"),
      @ApiResponse(code = 400, message = "Bad Request", response = ErrorDetails.class),
      @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDetails.class),
      @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDetails.class)})

  @GetMapping(value = "/product/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<ProductPriceDetails> getProductPriceById(@PathVariable("id") Long productId)
      throws Exception {

    LOGGER.info("In getProductPriceById for productId {}", productId);
    return productPriceAgrregatorService.getDetails(productId);
  }



}
