package com.gilberto.invoiceapi.clients;

import com.gilberto.invoiceapi.exceptions.ApiException;
import com.gilberto.invoiceapi.models.dto.responses.ProductResponse;

public interface ILogiStockClient {
  
  ProductResponse getProductByBarcode(String barcode) throws ApiException;
  
  void decreaseProductStock(Long id, Integer stockQuantity) throws ApiException;
  
}
