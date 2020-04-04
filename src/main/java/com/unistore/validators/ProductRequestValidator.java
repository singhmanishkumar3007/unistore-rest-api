package com.unistore.validators;

import java.util.ArrayList;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.unistore.entity.ProductEntity;

@Component("productRequestValidator")
public class ProductRequestValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return (ProductEntity.class.equals(clazz) || ArrayList.class.equals(clazz));
  }

  @Override
  public void validate(Object target, Errors errors) {

    ProductEntity productDetails=(ProductEntity) target;
    if (!StringUtils.isEmpty(productDetails.getSellerId())) {
      if (productDetails.getSellerId().matches(".*[a-zA-Z0-9_]+.*")) {
        errors.rejectValue("seller id", "invalid characters", new Object[] {"'userName'"},
            "seller id can contain only alphabets or digits");
      }
    }

    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "manufacturer", "manufacturer is required");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "sellerId", "sellerId is required");
  }

}
