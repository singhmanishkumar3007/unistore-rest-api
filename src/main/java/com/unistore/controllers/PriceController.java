package com.unistore.controllers;

import java.util.Arrays;
import java.util.List;
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
import com.unistore.domain.PaginatedResult;
import com.unistore.entity.PriceEntity;
import com.unistore.exception.StandardError;
import com.unistore.exception.StandardErrorCode;
import com.unistore.exception.StandardException;
import com.unistore.service.PriceDetailsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/price")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PriceController {

  private final PriceDetailsService priceDetailsService;

  @Qualifier("pricetRequestValidator")
  private final Validator validator;


  @InitBinder
  private void initBinder(WebDataBinder binder) {
    binder.setValidator(validator);
  }


  @ApiOperation(httpMethod = "POST", value = "Add price", response = String.class)
  @ApiResponses(value = {@ApiResponse(code = 201, message = "price added succesfully"),
      @ApiResponse(code = 400, message = "Bad Request", response = StandardError.class),
      @ApiResponse(code = 401, message = "Unauthorized", response = StandardError.class),
      @ApiResponse(code = 500, message = "Internal Server Error", response = StandardError.class)})

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PriceEntity> addPrice(@Validated @RequestBody PriceEntity priceEntity,
      BindingResult bindingResult) throws Exception {

    if (bindingResult.hasErrors()) {
      LOGGER.error("Validation failure for Request Body in add price");
      List<String[]> objectErrors = bindingResult.getAllErrors().stream()
          .map(bindingError -> bindingError.getCodes()).collect(Collectors.toList());
      String errors =
          objectErrors.stream().map(objectErrorArray -> String.join(",", objectErrorArray))
              .collect(Collectors.joining());
      StandardError standardError =
          StandardError.builder().method("addPrice").field("requestbody").message(errors).build();
      StandardException standardException =
          new StandardException(HttpStatus.BAD_REQUEST, Arrays.asList(standardError),
              StandardErrorCode.SC400, new Throwable("Bad Message request"));
      throw standardException;
    }

    LOGGER.info("adding new price {}", priceEntity);
    return new ResponseEntity<PriceEntity>(priceDetailsService.addPrice(priceEntity),
        HttpStatus.CREATED);
  }


  @ApiOperation(httpMethod = "GET", value = "Fetch price by product id", response = String.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "price details for product fetched succesfully"),
      @ApiResponse(code = 400, message = "Bad Request", response = StandardError.class),
      @ApiResponse(code = 401, message = "Unauthorized", response = StandardError.class),
      @ApiResponse(code = 500, message = "Internal Server Error", response = StandardError.class)})

  @GetMapping(value = "/product/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PaginatedResult<PriceEntity>> getPriceByProductId(
      @PathVariable("productId") Long productId,
      @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
      @RequestParam(value = "per_page", required = false, defaultValue = "10") Integer perPage)
      throws Exception {

    PaginatedResult<PriceEntity> paginatedResult =
        priceDetailsService.findPriceByProductId(productId, page, perPage);
    return new ResponseEntity<PaginatedResult<PriceEntity>>(paginatedResult, HttpStatus.OK);
  }

}
