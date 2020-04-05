package com.unistore.service;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.Optional;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import com.unistore.domain.ProductDomain;
import com.unistore.entity.ProductEntity;
import com.unistore.exception.UnistoreErrorCode;
import com.unistore.exception.UnistoreException;
import com.unistore.repository.ProductRepository;
import com.unistore.service.impl.ProductDetailsServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsServiceTest {

  private ProductDetailsService productDetailsService;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Mock
  private ProductRepository productRepositoryMock;

  private ProductDomain productDomain;
  private ProductEntity expectedProductEntity;


  @Before
  public void setUp() {
    productDomain = ProductDomain.builder().build();
    productDetailsService = new ProductDetailsServiceImpl(productRepositoryMock);
  }


  @Test
  public void testAddProduct() throws Exception {
    expectedProductEntity = new ProductEntity();
    BeanUtils.copyProperties(productDomain, expectedProductEntity);
    expectedProductEntity.setId(1L);
    Mockito.when(productRepositoryMock.save(Mockito.any())).thenReturn(expectedProductEntity);
    ProductEntity savedProduct = productDetailsService.addProduct(productDomain);
    assertEquals(expectedProductEntity, savedProduct);

  }


  @Test
  public void testAddProductExpectingExceptionInDBLayer() throws Exception {
    expectedProductEntity = new ProductEntity();
    BeanUtils.copyProperties(productDomain, expectedProductEntity);
    expectedProductEntity.setId(1L);
    Mockito.when(productRepositoryMock.save(Mockito.any()))
        .thenThrow(new DataAccessResourceFailureException("DB down", new Throwable("DB is down")));
    thrown.expect(UnistoreException.class);
    thrown.expect(hasProperty("httpStatus", is(HttpStatus.INTERNAL_SERVER_ERROR)));
    thrown.expect(hasProperty("standardErrorCode", is(UnistoreErrorCode.SC500)));
    thrown.expect(hasProperty("message", is(
        "Exception while saving new product :DB down; nested exception is java.lang.Throwable: DB is down")));
    productDetailsService.addProduct(productDomain);

  }

  @Test
  public void testFindProductById() throws Exception {
    expectedProductEntity = new ProductEntity();
    BeanUtils.copyProperties(productDomain, expectedProductEntity);
    expectedProductEntity.setId(1L);
    Optional<ProductEntity> expectedResult = Optional.of(expectedProductEntity);
    Mockito.when(productRepositoryMock.findById(Mockito.any())).thenReturn(expectedResult);
    ProductEntity fetchedProduct = productDetailsService.findProductById(1L);
    assertEquals(expectedResult.get(), fetchedProduct);

  }


  @Test
  public void testFindProductByIdExpectingNoData() throws Exception {
    expectedProductEntity = new ProductEntity();
    BeanUtils.copyProperties(productDomain, expectedProductEntity);
    expectedProductEntity.setId(1L);
    Optional<ProductEntity> expectedResult = Optional.empty();
    Mockito.when(productRepositoryMock.findById(Mockito.any())).thenReturn(expectedResult);
    ProductEntity fetchedProduct = productDetailsService.findProductById(1L);
    assertNull(fetchedProduct);

  }


  @Test
  public void testFindProductByIdExceptionInDBLayer() throws Exception {
    expectedProductEntity = new ProductEntity();
    BeanUtils.copyProperties(productDomain, expectedProductEntity);
    expectedProductEntity.setId(1L);
    Mockito.when(productRepositoryMock.findById(Mockito.any())).thenThrow(
        new DataAccessResourceFailureException("DB down", new Throwable("Query issues")));
    thrown.expect(UnistoreException.class);
    thrown.expect(hasProperty("httpStatus", is(HttpStatus.INTERNAL_SERVER_ERROR)));
    thrown.expect(hasProperty("standardErrorCode", is(UnistoreErrorCode.SC500)));
    thrown.expect(hasProperty("message", is(
        "Exception while fetching products by  id :1--DB down; nested exception is java.lang.Throwable: Query issues")));
    productDetailsService.findProductById(1L);

  }

}
