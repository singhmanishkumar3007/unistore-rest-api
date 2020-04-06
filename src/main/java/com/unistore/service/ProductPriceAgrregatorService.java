package com.unistore.service;

import com.unistore.domain.ProductPriceDetails;
import com.unistore.exception.UnistoreException;
import io.reactivex.Observable;

public interface ProductPriceAgrregatorService {


  Observable<ProductPriceDetails> getProductPriceDetails(Long productId) throws UnistoreException;

}
