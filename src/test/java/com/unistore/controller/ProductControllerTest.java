package com.unistore.controller;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import com.unistore.domain.ProductDomain;
import com.unistore.entity.ProductEntity;
import com.unistore.exception.UnistoreErrorCode;
import com.unistore.exception.UnistoreException;
import com.unistore.service.ProductDetailsService;

@RunWith(MockitoJUnitRunner.class)
public class ProductControllerTest {

  private ProductController productController;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Mock
  private ProductDetailsService productDetailsServiceMock;

  @Autowired
  @Qualifier("productRequestValidator")
  private Validator validator;

  private BindingResult bindingResult;

  private ProductDomain productDomain;

  private ProductEntity expectedProductEntity;

  @Before
  public void setUp() {
    bindingResult = mock(BindingResult.class);
    productDomain = ProductDomain.builder().build();
    productController = new ProductController(productDetailsServiceMock, validator);
  }


  @Test
  public void testAddProduct() throws Exception {
    expectedProductEntity = new ProductEntity();
    BeanUtils.copyProperties(productDomain, expectedProductEntity);
    expectedProductEntity.setId(1L);
    Mockito.when(productDetailsServiceMock.addProduct(productDomain))
        .thenReturn(expectedProductEntity);
    ResponseEntity<ProductEntity> actualResponse =
        productController.addProduct(productDomain, bindingResult);
    assertEquals(expectedProductEntity, actualResponse.getBody());

  }

  @Test
  public void testAddProductExpectingValidationBindingFailure() throws Exception {
    Mockito.when(bindingResult.hasErrors()).thenReturn(true);

    thrown.expect(UnistoreException.class);
    thrown.expect(hasProperty("httpStatus", is(HttpStatus.BAD_REQUEST)));
    thrown.expect(hasProperty("standardErrorCode", is(UnistoreErrorCode.SC400)));
    thrown.expect(hasProperty("message", is("")));
    productController.addProduct(productDomain, bindingResult);

  }

  @Test
  public void testAddProductExpectingExceptionInServiceLayer() throws Exception {
    expectedProductEntity = new ProductEntity();
    BeanUtils.copyProperties(productDomain, expectedProductEntity);
    expectedProductEntity.setId(1L);
    Mockito.when(productDetailsServiceMock.addProduct(productDomain))
        .thenThrow(new UnistoreException(HttpStatus.INTERNAL_SERVER_ERROR, UnistoreErrorCode.SC500,
            "Exception while saving new products :", new Throwable("Exception in DB")));
    thrown.expect(UnistoreException.class);
    thrown.expect(hasProperty("httpStatus", is(HttpStatus.INTERNAL_SERVER_ERROR)));
    thrown.expect(hasProperty("standardErrorCode", is(UnistoreErrorCode.SC500)));
    thrown.expect(hasProperty("message", is("Exception while saving new products :")));
    productController.addProduct(productDomain, bindingResult);

  }


  @Test
  public void testGetProductById() throws Exception {
    expectedProductEntity = ProductEntity.builder().id(1L).build();
    BeanUtils.copyProperties(productDomain, expectedProductEntity);
    expectedProductEntity.setId(1L);
    Mockito.when(productDetailsServiceMock.findProductById(Mockito.anyLong()))
        .thenReturn(expectedProductEntity);
    ProductEntity actualResponse = productController.getProductById(1L);
    assertEquals(expectedProductEntity, actualResponse);
    assertEquals(expectedProductEntity.getId(), actualResponse.getId());

  }

  @Test
  public void testGetProductByIdExpectingNoResult() throws Exception {
    Mockito.when(productDetailsServiceMock.findProductById(Mockito.anyLong())).thenReturn(null);
    ProductEntity actualResponse = productController.getProductById(1L);
    assertNull(actualResponse);

  }

  @Test
  public void testGetProductByIdExpectingExceptionInServiceLayer() throws Exception {

    Mockito.when(productDetailsServiceMock.findProductById(Mockito.anyLong()))
        .thenThrow(new UnistoreException(HttpStatus.INTERNAL_SERVER_ERROR, UnistoreErrorCode.SC500,
            "Exception while fetching products by  id :1", new Throwable("Exception in DB")));
    thrown.expect(UnistoreException.class);
    thrown.expect(hasProperty("httpStatus", is(HttpStatus.INTERNAL_SERVER_ERROR)));
    thrown.expect(hasProperty("standardErrorCode", is(UnistoreErrorCode.SC500)));
    thrown.expect(hasProperty("message", is("Exception while fetching products by  id :1")));
    productController.getProductById(1L);

  }



}
