package com.unistore.controller;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import java.util.Arrays;
import java.util.Collections;
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
import com.unistore.domain.PaginatedResult;
import com.unistore.entity.PriceEntity;
import com.unistore.exception.UnistoreErrorCode;
import com.unistore.exception.UnistoreException;
import com.unistore.service.PriceDetailsService;

@RunWith(MockitoJUnitRunner.class)
public class PriceControllerTest {

  private PriceController priceController;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Mock
  private PriceDetailsService priceDetailsServiceMock;

  @Autowired
  @Qualifier("priceRequestValidator")
  private Validator validator;

  private BindingResult bindingResult;

  private PriceEntity toBeAddedPriceEntity;

  private PriceEntity expectedPriceEntity;

  private PaginatedResult<PriceEntity> expectedPriceEntityPaginated;

  @Before
  public void setUp() {
    bindingResult = mock(BindingResult.class);
    priceController = new PriceController(priceDetailsServiceMock, validator);
  }


  @Test
  public void testAddPrice() throws Exception {
    toBeAddedPriceEntity = PriceEntity.builder().productId(1L).build();
    expectedPriceEntity = new PriceEntity();
    BeanUtils.copyProperties(toBeAddedPriceEntity, expectedPriceEntity);
    expectedPriceEntity.setPriceId(1L);
    Mockito.when(priceDetailsServiceMock.addPrice(toBeAddedPriceEntity))
        .thenReturn(expectedPriceEntity);
    ResponseEntity<PriceEntity> actualResponse =
        priceController.addPrice(toBeAddedPriceEntity, bindingResult);
    assertEquals(expectedPriceEntity, actualResponse.getBody());

  }

  @Test
  public void testAddPriceExpectingValidationBindingFailure() throws Exception {
    Mockito.when(bindingResult.hasErrors()).thenReturn(true);

    thrown.expect(UnistoreException.class);
    thrown.expect(hasProperty("httpStatus", is(HttpStatus.BAD_REQUEST)));
    thrown.expect(hasProperty("standardErrorCode", is(UnistoreErrorCode.SC400)));
    thrown.expect(hasProperty("message", is("")));
    priceController.addPrice(toBeAddedPriceEntity, bindingResult);

  }

  @Test
  public void testAddPriceExpectingExceptionInServiceLayer() throws Exception {
    toBeAddedPriceEntity = PriceEntity.builder().productId(1L).build();
    Mockito.when(priceDetailsServiceMock.addPrice(toBeAddedPriceEntity))
        .thenThrow(new UnistoreException(HttpStatus.INTERNAL_SERVER_ERROR, UnistoreErrorCode.SC500,
            "Exception while saving new price :", new Throwable("Exception in DB")));
    thrown.expect(UnistoreException.class);
    thrown.expect(hasProperty("httpStatus", is(HttpStatus.INTERNAL_SERVER_ERROR)));
    thrown.expect(hasProperty("standardErrorCode", is(UnistoreErrorCode.SC500)));
    thrown.expect(hasProperty("message", is("Exception while saving new price :")));
    priceController.addPrice(toBeAddedPriceEntity, bindingResult);

  }


  @Test
  public void testFindPriceByProductId() throws Exception {
    toBeAddedPriceEntity = PriceEntity.builder().productId(1L).build();
    expectedPriceEntity = new PriceEntity();
    BeanUtils.copyProperties(toBeAddedPriceEntity, expectedPriceEntity);
    expectedPriceEntity.setPriceId(1L);
    expectedPriceEntityPaginated = new PaginatedResult<>(1L, Arrays.asList(expectedPriceEntity));
    Mockito.when(priceDetailsServiceMock.findPriceByProductId(Mockito.anyLong(), Mockito.any(),
        Mockito.any())).thenReturn(expectedPriceEntityPaginated);
    ResponseEntity<PaginatedResult<PriceEntity>> actualResponse =
        priceController.getPriceByProductId(1L, 1, 10);
    assertEquals(expectedPriceEntity, actualResponse.getBody().getResult().get(0));
    assertEquals(expectedPriceEntity.getPriceId(),
        actualResponse.getBody().getResult().get(0).getPriceId());

  }

  @SuppressWarnings("unchecked")
  @Test
  public void testFindPriceByProductIdExpectingNoResult() throws Exception {
    toBeAddedPriceEntity = PriceEntity.builder().productId(1L).build();
    expectedPriceEntityPaginated = new PaginatedResult<>(0L, Collections.EMPTY_LIST);
    Mockito.when(priceDetailsServiceMock.findPriceByProductId(Mockito.anyLong(), Mockito.any(),
        Mockito.any())).thenReturn(expectedPriceEntityPaginated);
    ResponseEntity<PaginatedResult<PriceEntity>> actualResponse =
        priceController.getPriceByProductId(1L, 1, 10);
    assertEquals(expectedPriceEntityPaginated.getResult(), actualResponse.getBody().getResult());
    assertEquals(0, actualResponse.getBody().getResult().size());

  }

  @Test
  public void testFindPriceByProductIdExpectingExceptionInServiceLayer() throws Exception {

    Mockito
        .when(priceDetailsServiceMock.findPriceByProductId(Mockito.anyLong(), Mockito.any(),
            Mockito.any()))
        .thenThrow(new UnistoreException(HttpStatus.INTERNAL_SERVER_ERROR, UnistoreErrorCode.SC500,
            "Exception while fetching price by  id :1", new Throwable("Exception in DB")));
    thrown.expect(UnistoreException.class);
    thrown.expect(hasProperty("httpStatus", is(HttpStatus.INTERNAL_SERVER_ERROR)));
    thrown.expect(hasProperty("standardErrorCode", is(UnistoreErrorCode.SC500)));
    thrown.expect(hasProperty("message", is("Exception while fetching price by  id :1")));
    priceController.getPriceByProductId(1L, 1, 10);

  }



}
