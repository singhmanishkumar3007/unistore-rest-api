package com.unistore.validators;

import java.util.ArrayList;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.unistore.entity.PriceEntity;

@Component("priceRequestValidator")
public class PriceRequestValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return (PriceEntity.class.equals(clazz) || ArrayList.class.equals(clazz));
  }

  @Override
  public void validate(Object target, Errors errors) {

    PriceEntity priceDetails = (PriceEntity) target;
    if (!StringUtils.isEmpty(priceDetails.getProductId())) {
      if (String.valueOf(priceDetails.getProductId()).matches(".*[0-9_]+.*")) {
        errors.rejectValue("product id", "invalid characters", new Object[] {"'productId'"},
            "product id can contain only digits");
      }
    }

    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "productId", "product id is required");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "range", "range is required");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "min", "min is required");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "max", "max is required");
  }

}
