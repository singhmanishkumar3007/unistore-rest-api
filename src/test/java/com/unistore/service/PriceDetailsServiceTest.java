package com.unistore.service;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import com.unistore.domain.PaginatedResult;
import com.unistore.entity.PriceEntity;
import com.unistore.exception.UnistoreErrorCode;
import com.unistore.exception.UnistoreException;
import com.unistore.repository.PriceRepository;
import com.unistore.service.impl.PriceDetailsServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class PriceDetailsServiceTest {

  private PriceDetailsService priceDetailsService;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Mock
  private PriceRepository priceRepositoryMock;

  private PriceEntity toBeAddedPriceEntity;


  private PriceEntity expectedPriceEntity;


  @Before
  public void setUp() {
    toBeAddedPriceEntity = PriceEntity.builder().productId(1L).build();
    priceDetailsService = new PriceDetailsServiceImpl(priceRepositoryMock);
  }


  @Test
  public void testAddPrice() throws Exception {
    expectedPriceEntity = new PriceEntity();
    BeanUtils.copyProperties(toBeAddedPriceEntity, expectedPriceEntity);
    expectedPriceEntity.setPriceId(1L);
    Mockito.when(priceRepositoryMock.save(Mockito.any())).thenReturn(expectedPriceEntity);
    PriceEntity savedPrice = priceDetailsService.addPrice(toBeAddedPriceEntity);
    assertEquals(expectedPriceEntity, savedPrice);

  }


  @Test
  public void testAddPriceExpectingExceptionInDBLayer() throws Exception {
    expectedPriceEntity = new PriceEntity();
    BeanUtils.copyProperties(toBeAddedPriceEntity, expectedPriceEntity);
    expectedPriceEntity.setPriceId(1L);
    Mockito.when(priceRepositoryMock.save(Mockito.any()))
        .thenThrow(new DataAccessResourceFailureException("DB down", new Throwable("DB is down")));
    thrown.expect(UnistoreException.class);
    thrown.expect(hasProperty("httpStatus", is(HttpStatus.INTERNAL_SERVER_ERROR)));
    thrown.expect(hasProperty("standardErrorCode", is(UnistoreErrorCode.SC500)));
    thrown.expect(hasProperty("message", is(
        "Exception while saving new price :DB down; nested exception is java.lang.Throwable: DB is down")));
    priceDetailsService.addPrice(toBeAddedPriceEntity);

  }

  @Test
  public void testFindPriceByProductId() throws Exception {
    expectedPriceEntity = new PriceEntity();
    BeanUtils.copyProperties(toBeAddedPriceEntity, expectedPriceEntity);
    expectedPriceEntity.setPriceId(1L);
    Page<PriceEntity> expectedResult = new Page<PriceEntity>() {

      @Override
      public int getNumber() {
        // TODO Auto-generated method stub
        return 1;
      }

      @Override
      public int getSize() {
        // TODO Auto-generated method stub
        return 1;
      }

      @Override
      public int getNumberOfElements() {
        // TODO Auto-generated method stub
        return 1;
      }

      @Override
      public List<PriceEntity> getContent() {
        // TODO Auto-generated method stub
        return Arrays.asList(expectedPriceEntity);
      }

      @Override
      public boolean hasContent() {
        // TODO Auto-generated method stub
        return true;
      }

      @Override
      public Sort getSort() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public boolean isFirst() {
        // TODO Auto-generated method stub
        return true;
      }

      @Override
      public boolean isLast() {
        // TODO Auto-generated method stub
        return true;
      }

      @Override
      public boolean hasNext() {
        // TODO Auto-generated method stub
        return false;
      }

      @Override
      public boolean hasPrevious() {
        // TODO Auto-generated method stub
        return false;
      }

      @Override
      public Pageable nextPageable() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public Pageable previousPageable() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public Iterator<PriceEntity> iterator() {
        // TODO Auto-generated method stub
        return Arrays.asList(expectedPriceEntity).iterator();
      }

      @Override
      public int getTotalPages() {
        // TODO Auto-generated method stub
        return 1;
      }

      @Override
      public long getTotalElements() {
        // TODO Auto-generated method stub
        return 1;
      }

      @Override
      public <U> Page<U> map(Function<? super PriceEntity, ? extends U> converter) {
        // TODO Auto-generated method stub
        return null;
      }
    };
    Mockito.when(priceRepositoryMock.findByProductId(Mockito.any(), Mockito.any()))
        .thenReturn(expectedResult);
    PaginatedResult<PriceEntity> fetchedPrice = priceDetailsService.findPriceByProductId(1L, 1, 10);
    assertEquals(expectedResult.get().count(), fetchedPrice.getTotalCount().longValue());

  }



  @Test
  public void testFindPriceByProductIdExceptionInDBLayer() throws Exception {
    expectedPriceEntity = new PriceEntity();
    BeanUtils.copyProperties(toBeAddedPriceEntity, expectedPriceEntity);
    expectedPriceEntity.setPriceId(1L);
    Mockito.when(priceRepositoryMock.findByProductId(Mockito.any(), Mockito.any())).thenThrow(
        new DataAccessResourceFailureException("DB down", new Throwable("Query issues")));
    thrown.expect(UnistoreException.class);
    thrown.expect(hasProperty("httpStatus", is(HttpStatus.INTERNAL_SERVER_ERROR)));
    thrown.expect(hasProperty("standardErrorCode", is(UnistoreErrorCode.SC500)));
    thrown.expect(hasProperty("message", is(
        "Exception while finding price of product id 1 :DB down; nested exception is java.lang.Throwable: Query issues")));
    priceDetailsService.findPriceByProductId(1L, 1, 10);

  }

}
