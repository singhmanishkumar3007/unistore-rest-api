package com.unistore.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Entity
@Table(name = "price_details")
public class PriceEntity {

  @Id
  @Column(name = "price_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long priceId;

  @Column(name = "product_id")
  private Long productId;

  @Column(name = "price_start_date")
  private String priceStartDate;

  @Column(name = "price_end_date")
  private String priceEndDate;

  @Embedded
  private Price price;



}


@Embeddable
@Data
class Price {
  private String range;
  private Double min;
  private Double max;
}


