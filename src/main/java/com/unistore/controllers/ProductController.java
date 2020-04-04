package com.unistore.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unistore.domain.PaginatedResult;
import com.unistore.entity.ProductEntity;
import com.unistore.exception.StandardError;
import com.unistore.exception.StandardErrorCode;
import com.unistore.exception.StandardException;
import com.unistore.service.ProductDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductController {

  private final ProductDetailsService productDetailsService;

  @Qualifier("productRequestValidator")
  private final Validator validator;


  @InitBinder
  private void initBinder(WebDataBinder binder) {
    binder.setValidator(validator);
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ProductEntity> addProduct(
      @Validated @RequestBody ProductEntity productEntity, BindingResult bindingResult)
      throws Exception {

    if (bindingResult.hasErrors()) {
      LOGGER.error("Validation failure for Request Body in add product");
      List<String[]> objectErrors = bindingResult.getAllErrors().stream()
          .map(bindingError -> bindingError.getCodes()).collect(Collectors.toList());
      String errors =
          objectErrors.stream().map(objectErrorArray -> String.join(",", objectErrorArray))
              .collect(Collectors.joining());
      StandardError standardError =
          StandardError.builder().method("addProduct").field("requestbody").message(errors).build();
      StandardException standardException =
          new StandardException(HttpStatus.BAD_REQUEST, Arrays.asList(standardError),
              StandardErrorCode.SC400, new Throwable("Bad Message request"));
      throw standardException;
    }

    LOGGER.info("adding new product {}", productEntity);
    return new ResponseEntity<ProductEntity>(productDetailsService.addProduct(productEntity),
        HttpStatus.CREATED);
  }

  @PostMapping(value = "/multiple", produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<Long, ProductEntity>> addMultipleProducts(
      @RequestBody List<ProductEntity> productDetails) throws Exception {

    LOGGER.info("list of products to be added is {}",
        new ObjectMapper().writeValueAsString(productDetails));
    return new ResponseEntity<Map<Long, ProductEntity>>(
        productDetailsService.addMultipleProducts(productDetails), HttpStatus.CREATED);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PaginatedResult<ProductEntity>> getAllProducts(
      @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
      @RequestParam(value = "per_page", required = false, defaultValue = "10") Integer perPage)
      throws Exception {

    PaginatedResult<ProductEntity> paginatedResult =
        productDetailsService.findProducts(page, perPage);

    return new ResponseEntity<PaginatedResult<ProductEntity>>(paginatedResult, HttpStatus.OK);
  }

  @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ProductEntity getProductById(@PathVariable("id") Long ProductId) throws Exception {

    return productDetailsService.findProductById(ProductId);
  }

  @GetMapping(value = "/seller/{sellerId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PaginatedResult<ProductEntity>> getProductsBySellerId(
      @PathVariable("sellerId") String sellerId,
      @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
      @RequestParam(value = "per_page", required = false, defaultValue = "10") Integer perPage)
      throws Exception {

    PaginatedResult<ProductEntity> paginatedResult =
        productDetailsService.findProductBySellerId(sellerId, page, perPage);
    return new ResponseEntity<PaginatedResult<ProductEntity>>(paginatedResult, HttpStatus.OK);
  }

}
