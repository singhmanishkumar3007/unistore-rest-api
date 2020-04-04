package com.unistore.entity;

import java.sql.Date;
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
@Table(name = "product_details")
public class ProductEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "seller_id", unique = true)
  private String sellerId;

  @Column(name = "title")
  private String title;

  @Column(name = "manufacturer")
  private String manufacturer;

  @Column(name = "is_sold_out")
  private Boolean soldOut;

  @Column(name = "is_back_order")
  private Boolean backOrder;

  @Column(name = "requires_shopping")
  private Boolean requireShopping;

  @Column(name = "visible")
  private Boolean visible;

  @Embedded
  private Workflow workflow;

  @Embedded
  private PublishedAt publishedAt;

  @Embedded
  private Metafields[] metafields;

  @Embedded
  private CreatedAt createdAt;

  @Embedded
  private UpdatedAt updatedAt;

}


@Embeddable
@Data
class Workflow {
  @Column(name = "status")
  private String status;
}

@Embeddable
@Data
class PublishedAt {
  @Column(name = "publishedDate")
  private Date publishedDate;

}

@Embeddable
@Data
class Metafields {
  
  @Column(name = "value")
  private String value;
  @Column(name = "key")
  private String key;

}

@Embeddable
@Data
class CreatedAt {
  @Column(name = "createdDate")
  private Date createdDate;

}

@Embeddable
@Data
class UpdatedAt {
  @Column(name = "updatedDate")
  private Date updatedDate;

}
