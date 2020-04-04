package com.unistore.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.unistore.domain.PaginatedResult;
import com.unistore.entity.ProductEntity;
import com.unistore.repository.ProductRepository;
import com.unistore.service.ProductDetailsService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductDetailsServiceImpl implements ProductDetailsService {

  private final ProductRepository productRepository;



  @Override
  public ProductEntity addProduct(ProductEntity productEntity) {

    ProductEntity productAdded = productRepository.save(productEntity);
    return productAdded;
  }

  @Override
  public Map<Long, ProductEntity> addMultipleProducts(List<ProductEntity> productEntity) {
    List<ProductEntity> productEntitiesAdded =
        (List<ProductEntity>) productRepository.saveAll(productEntity);
    Map<Long, ProductEntity> addedProductsMap = productEntitiesAdded.stream()
        .collect(Collectors.toMap(ProductEntity::getId, product -> product));
    return addedProductsMap;
  }

  @SuppressWarnings("deprecation")
  @Override
  public PaginatedResult<ProductEntity> findProducts(Integer page, Integer perPage) {

    Pageable pageable = new PageRequest(page, perPage);
    Page<ProductEntity> productEntitiesInPage = productRepository.findAll(pageable);
    Long totalNumberOfPages = productEntitiesInPage.getTotalElements();
    List<ProductEntity> productList = productEntitiesInPage.getContent();
    PaginatedResult<ProductEntity> paginatedProducts =
        new PaginatedResult<>(totalNumberOfPages, productList);
    return paginatedProducts;
  }

  @SuppressWarnings("deprecation")
  @Override
  public PaginatedResult<ProductEntity> findProductBySellerId(String sellerId, Integer page,
      Integer perPage) {
    Pageable pageable = new PageRequest(page, perPage);
    Page<ProductEntity> productEntitiesInPage =
        productRepository.findBySellerId(sellerId, pageable);
    Long totalNumberOfPages = productEntitiesInPage.getTotalElements();
    List<ProductEntity> productList = productEntitiesInPage.getContent();
    PaginatedResult<ProductEntity> paginatedProducts =
        new PaginatedResult<>(totalNumberOfPages, productList);
    return paginatedProducts;
  }

  @Override
  public ProductEntity findProductById(Long id) {

    ProductEntity productData = productRepository.findById(id).orElse(null);
    return productData;
  }

}
