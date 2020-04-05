package com.unistore.controllers;

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
import com.unistore.domain.ProductDomain;
import com.unistore.entity.ProductEntity;
import com.unistore.exception.ErrorDetails;
import com.unistore.exception.UnistoreErrorCode;
import com.unistore.exception.UnistoreException;
import com.unistore.service.ProductDetailsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

  @ApiOperation(httpMethod = "POST", value = "Add a product", response = String.class)
  @ApiResponses(value = {@ApiResponse(code = 201, message = "New product Created succesfully"),
      @ApiResponse(code = 400, message = "Bad Request", response = ErrorDetails.class),
      @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDetails.class),
      @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDetails.class)})

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ProductEntity> addProduct(
      @Validated @RequestBody ProductDomain productDomain, BindingResult bindingResult)
      throws Exception {

    if (bindingResult.hasErrors()) {
      LOGGER.error("Validation failure for Request Body in add product");
      List<String[]> objectErrors = bindingResult.getAllErrors().stream()
          .map(bindingError -> bindingError.getCodes()).collect(Collectors.toList());
      String errors =
          objectErrors.stream().map(objectErrorArray -> String.join(",", objectErrorArray))
              .collect(Collectors.joining());
      UnistoreException standardException = new UnistoreException(HttpStatus.BAD_REQUEST,
          UnistoreErrorCode.SC400, errors, new Throwable("Bad Message request"));
      throw standardException;
    }

    LOGGER.info("adding new product {}", productDomain);
    return new ResponseEntity<ProductEntity>(productDetailsService.addProduct(productDomain),
        HttpStatus.CREATED);
  }


  @ApiOperation(httpMethod = "POST", value = "Add multiple products", response = String.class)
  @ApiResponses(value = {@ApiResponse(code = 201, message = "products Created succesfully"),
      @ApiResponse(code = 400, message = "Bad Request", response = ErrorDetails.class),
      @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDetails.class),
      @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDetails.class)})

  @PostMapping(value = "/multiple", produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<Long, ProductEntity>> addMultipleProducts(
      @RequestBody List<ProductDomain> productDetails) throws Exception {

    LOGGER.info("list of products to be added is {}",
        new ObjectMapper().writeValueAsString(productDetails));
    return new ResponseEntity<Map<Long, ProductEntity>>(
        productDetailsService.addMultipleProducts(productDetails), HttpStatus.CREATED);
  }

  @ApiOperation(httpMethod = "GET", value = "Fetch All products", response = String.class)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Products fetched succesfully"),
      @ApiResponse(code = 400, message = "Bad Request", response = ErrorDetails.class),
      @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDetails.class),
      @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDetails.class)})

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PaginatedResult<ProductEntity>> getAllProducts(
      @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
      @RequestParam(value = "per_page", required = false, defaultValue = "10") Integer perPage)
      throws Exception {

    PaginatedResult<ProductEntity> paginatedResult =
        productDetailsService.findProducts(page, perPage);

    return new ResponseEntity<PaginatedResult<ProductEntity>>(paginatedResult, HttpStatus.OK);
  }


  @ApiOperation(httpMethod = "GET", value = "Fetch product by product id", response = String.class)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "product details fetched succesfully"),
      @ApiResponse(code = 400, message = "Bad Request", response = ErrorDetails.class),
      @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDetails.class),
      @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDetails.class)})

  @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ProductEntity getProductById(@PathVariable("id") Long ProductId) throws Exception {

    return productDetailsService.findProductById(ProductId);
  }


  @ApiOperation(httpMethod = "GET", value = "Fetch products by seller id", response = String.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "product details fetched succesfully by seller id"),
      @ApiResponse(code = 400, message = "Bad Request", response = ErrorDetails.class),
      @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDetails.class),
      @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDetails.class)})

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
