package com.gilberto.invoiceapi.services;

import com.gilberto.invoiceapi.exceptions.ApiException;
import com.gilberto.invoiceapi.exceptions.InvoiceNotFoundException;
import com.gilberto.invoiceapi.models.dto.requests.InvoiceFilter;
import com.gilberto.invoiceapi.models.dto.requests.InvoiceForm;
import com.gilberto.invoiceapi.models.dto.responses.InvoiceDTO;
import java.util.List;

public interface IInvoiceService {
  
  InvoiceDTO create(InvoiceForm invoiceForm) throws ApiException;
  
  List<InvoiceDTO> listAll(String issuerTaxId, InvoiceFilter invoiceFilter);
  
  InvoiceDTO listById(String issuerTaxId, Long id) throws InvoiceNotFoundException;
  
  InvoiceDTO cancelInvoice(String issuerTaxId, Long id) throws InvoiceNotFoundException;
  
}
