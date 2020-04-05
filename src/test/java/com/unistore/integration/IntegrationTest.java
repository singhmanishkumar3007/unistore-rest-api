package com.unistore.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.unistore.UnistoreApplication;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DirtiesContext
@SpringBootTest(classes = UnistoreApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IntegrationTest {

  @Autowired
  private WebApplicationContext webApplicationContext;
  private MockMvc mockMvc;

  private String productRequestBody;

  private String priceRequestBody;
  private String productResponseBody;

  @Before
  public void setUp() {

    MockitoAnnotations.initMocks(this);
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    productRequestBody =
        "{\n" + "  \"back_order\": true,\n" + "  \n" + "  \n" + "  \"manufacturer\": \"string\",\n"
            + "  \"metafields\": {\n" + "    \"key\": \"string\",\n" + "    \"value\": \"string\"\n"
            + "  },\n" + "  \n" + "  \"require_shopping\": true,\n" + "  \"seller_id\": \"234\",\n"
            + "  \"sold_out\": true,\n" + "  \"title\": \"string\",\n" + " \n"
            + "  \"visible\": true,\n" + "  \"workflow\": {\n" + "    \"status\": \"new\"\n"
            + "  }\n" + "}";


    priceRequestBody = "{\n" + "    \"price_id\": 2,\n" + "    \"product_id\": 1,\n"
        + "    \"price\": {\n" + "        \"range\": \"4-11\",\n" + "        \"min\": 5.0,\n"
        + "        \"max\": 10.0\n" + "    }\n" + "}";

    productResponseBody =
        "{\"id\":1,\"seller_id\":\"234\",\"title\":\"string\",\"manufacturer\":\"string\",\"sold_out\":true,\"back_order\":true,\"require_shopping\":true,\"visible\":true,\"workflow\":{\"status\":\"new\"},\"published_at\":{\"published_date\":\"2020-04-05 15:59\"},\"metafields\":{\"value\":\"string\",\"key\":\"string\"},\"created_at\":{\"created_date\":\"2020-04-05 15:59\"}}";
  }

  @Test
  public void testAddProuct() throws Exception {
    mockMvc
        .perform(
            post("/product").contentType(MediaType.APPLICATION_JSON).content(productRequestBody))
        .andExpect(status().isCreated());
  }


  @Test
  public void testAddPrice() throws Exception {
    mockMvc
        .perform(post("/price").contentType(MediaType.APPLICATION_JSON).content(priceRequestBody))
        .andExpect(status().isCreated());
  }

  @Test
  public void testGetProDuct() throws Exception {

    mockMvc
        .perform(get("/product/id/1").contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }
  
  @Test
  public void testGetAllProducts() throws Exception {

    mockMvc
        .perform(get("/product").contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }
  
  @Test
  public void testGetProductsBySellerId() throws Exception {

    mockMvc
        .perform(get("/product/seller/1").contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }
  
  @Test
  public void testGetPrice() throws Exception {

    mockMvc
        .perform(get("/price/product/1").contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }
  
  @Test
  public void testGetProduct() throws Exception {

    mockMvc
        .perform(get("/product-price/1").contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
  }


}
