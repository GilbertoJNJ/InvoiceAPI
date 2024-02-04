package com.gilberto.invoiceapi.clients.implementations;

import com.gilberto.invoiceapi.clients.ILogiStockClient;
import com.gilberto.invoiceapi.exceptions.ApiException;
import com.gilberto.invoiceapi.models.dto.responses.ProductResponse;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LogiStockClient implements ILogiStockClient {
  
  @Value("${client.logistock.url}")
  private String url;
  
  private final HttpClient client = HttpClient.newHttpClient();
  
  @Override
  public ProductResponse getProductByBarcode(String barcode) throws ApiException {
    var uri = url + "/api/v1/product/barcode/" + barcode;
    
    try {
      var request = HttpRequest.newBuilder()
          .uri(URI.create(uri))
          .build();
      
      var response = this.client.send(request, HttpResponse.BodyHandlers.ofString());
      
      if (response.statusCode() != 200) {
        throw new ApiException(response.body());
      }
      var objectMapper = new ObjectMapper();
      return objectMapper.readValue(response.body(), ProductResponse.class);
      
    } catch (Exception e) {
      throw new ApiException("Error processing response: " + e.getMessage());
    }
  }
  
  @Override
  public void decreaseProductStock(Long id, Integer stockQuantity) throws ApiException {
    var uri         = url + "/api/v1/product/" + id + "/decrease";
    var requestBody = String.format("{\"quantity\": %d}", stockQuantity);
    
    try {
      var request = HttpRequest.newBuilder()
          .uri(URI.create(uri))
          .header("Content-Type", "application/json")
          .method("PATCH", HttpRequest.BodyPublishers.ofString(requestBody))
          .build();
      
      var response = this.client.send(request, HttpResponse.BodyHandlers.ofString());
      
      if (response.statusCode() != 200) {
        throw new ApiException(response.body());
      }
    } catch (Exception e) {
      throw new ApiException("Error processing response: " + e.getMessage());
    }
  }
  
}
