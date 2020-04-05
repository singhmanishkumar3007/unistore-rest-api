package com.unistore.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.unistore.domain.PaginatedResult;
import com.unistore.domain.ProductDomain;
import com.unistore.entity.CreatedAt;
import com.unistore.entity.ProductEntity;
import com.unistore.entity.PublishedAt;
import com.unistore.exception.UnistoreErrorCode;
import com.unistore.exception.UnistoreException;
import com.unistore.repository.ProductRepository;
import com.unistore.service.ProductDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ProductDetailsServiceImpl implements ProductDetailsService {

  private final ProductRepository productRepository;



  @Override
  public ProductEntity addProduct(ProductDomain productDomain) throws UnistoreException {

    ProductEntity productEntity = new ProductEntity();
    BeanUtils.copyProperties(productDomain, productEntity);
    productEntity.setCreatedAt(CreatedAt.builder().createdDate(LocalDateTime.now()).build());
    productEntity.setPublishedAt(PublishedAt.builder().publishedDate(LocalDateTime.now()).build());
    try {

      ProductEntity productAdded = productRepository.save(productEntity);
      LOGGER.info("New product added successfully with product id :{}", productAdded.getId());
      return productAdded;

    } catch (Exception e) {
      LOGGER.error("Exception while saving new product :", e);
      throw new UnistoreException(HttpStatus.INTERNAL_SERVER_ERROR, UnistoreErrorCode.SC500,
          "Exception while saving new product :" + e.getMessage(), e);
    }
  }

  @Override
  public Map<Long, ProductEntity> addMultipleProducts(List<ProductDomain> productDomain)
      throws UnistoreException {

    List<ProductEntity> productEntity = productDomain.stream()
        .map(this::transformProductDomainToProductEntity).collect(Collectors.toList());
    List<ProductEntity> productEntitiesAdded;
    try {
      productEntitiesAdded = (List<ProductEntity>) productRepository.saveAll(productEntity);
    } catch (Exception e) {
      LOGGER.error("Exception while saving new products :", e);
      throw new UnistoreException(HttpStatus.INTERNAL_SERVER_ERROR, UnistoreErrorCode.SC500,
          "Exception while saving new products :" + e.getMessage(), e);
    }
    Map<Long, ProductEntity> addedProductsMap = productEntitiesAdded.stream()
        .collect(Collectors.toMap(ProductEntity::getId, product -> product));
    return addedProductsMap;
  }

  private ProductEntity transformProductDomainToProductEntity(ProductDomain productDomain) {
    ProductEntity productEntity = new ProductEntity();
    BeanUtils.copyProperties(productDomain, productEntity);
    return productEntity;
  }

  @SuppressWarnings("deprecation")
  @Override
  public PaginatedResult<ProductEntity> findProducts(Integer page, Integer perPage)
      throws UnistoreException {

    Pageable pageable = new PageRequest(page, perPage);
    Page<ProductEntity> productEntitiesInPage;
    try {
      productEntitiesInPage = productRepository.findAll(pageable);
    } catch (Exception e) {
      LOGGER.error("Exception while fetching all products :", e);
      throw new UnistoreException(HttpStatus.INTERNAL_SERVER_ERROR, UnistoreErrorCode.SC500,
          "Exception while fetching all products :" + e.getMessage(), e);
    }
    Long totalNumberOfPages = productEntitiesInPage.getTotalElements();
    List<ProductEntity> productList = productEntitiesInPage.getContent();
    PaginatedResult<ProductEntity> paginatedProducts =
        new PaginatedResult<>(totalNumberOfPages, productList);
    return paginatedProducts;
  }

  @SuppressWarnings("deprecation")
  @Override
  public PaginatedResult<ProductEntity> findProductBySellerId(String sellerId, Integer page,
      Integer perPage) throws UnistoreException {
    Pageable pageable = new PageRequest(page, perPage);
    Page<ProductEntity> productEntitiesInPage;
    try {
      productEntitiesInPage = productRepository.findBySellerId(sellerId, pageable);
    } catch (Exception e) {
      LOGGER.error("Exception while fetching products by seller id : {} :", sellerId, e);
      throw new UnistoreException(HttpStatus.INTERNAL_SERVER_ERROR, UnistoreErrorCode.SC500,
          "Exception while fetching products by seller id :" + sellerId + e.getMessage(), e);
    }
    Long totalNumberOfPages = productEntitiesInPage.getTotalElements();
    List<ProductEntity> productList = productEntitiesInPage.getContent();
    PaginatedResult<ProductEntity> paginatedProducts =
        new PaginatedResult<>(totalNumberOfPages, productList);
    return paginatedProducts;
  }

  @Override
  public ProductEntity findProductById(Long id) throws UnistoreException {

    try {
      ProductEntity productData = productRepository.findById(id).orElse(null);
      return productData;
    } catch (Exception e) {
      LOGGER.error("Exception while fetching product by product id : {} ", id, e);
      throw new UnistoreException(HttpStatus.INTERNAL_SERVER_ERROR, UnistoreErrorCode.SC500,
          "Exception while fetching products by  id :" + id + e.getMessage(), e);
    }

  }

}
