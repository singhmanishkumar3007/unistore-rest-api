package com.unistore.service;

import java.util.List;
import com.unistore.domain.ProductPriceDetails;
import com.unistore.exception.UnistoreException;

public interface ProductPriceAgrregatorService {

  List<ProductPriceDetails> getDetails(Long productId) throws UnistoreException;

}
