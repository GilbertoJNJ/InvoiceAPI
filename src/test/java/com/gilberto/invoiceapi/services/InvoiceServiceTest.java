package com.gilberto.invoiceapi.services;

import com.gilberto.invoiceapi.clients.ILogiStockClient;
import com.gilberto.invoiceapi.exceptions.ApiException;
import com.gilberto.invoiceapi.exceptions.InvoiceNotFoundException;
import com.gilberto.invoiceapi.models.entity.Invoice;
import com.gilberto.invoiceapi.models.enums.Status;
import com.gilberto.invoiceapi.repositories.IInvoiceRepository;
import com.gilberto.invoiceapi.services.implementations.InvoiceService;
import com.gilberto.invoiceapi.utils.ModelUtil;
import java.util.Collections;
import java.util.Optional;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {
  
  public static final String ISSUER_TAX_ID      = "issuerTaxId";
  public static final long   INVOICE_ID         = 1L;
  public static final long   INVALID_INVOICE_ID = 2L;
  
  @Mock
  private IInvoiceRepository invoiceRepository;
  
  @Mock
  private ILogiStockClient logiStockClient;
  
  @InjectMocks
  private InvoiceService invoiceService;
  
  // Create
  @Test
  public void shouldCreateAnInvoice() throws ApiException {
    // Given
    var invoiceForm     = ModelUtil.getInvoiceForm();
    var productResponse = ModelUtil.getProductResponse();
    
    var invoiceCaptured = ArgumentCaptor.forClass(Invoice.class);
    
    // When
    when(this.invoiceRepository.findByIssuerTaxId(invoiceForm.issuerTaxId()))
        .thenReturn(Collections.emptyList());
    when(this.logiStockClient.getProductByBarcode(any()))
        .thenReturn(productResponse);
    doNothing().when(this.logiStockClient).decreaseProductStock(any(), any());
    when(this.invoiceRepository.save(invoiceCaptured.capture()))
        .thenAnswer(invocation -> invocation.getArgument(0));
    
    // Then
    var invoiceDTO = this.invoiceService.create(invoiceForm);
    
    assertThat(invoiceDTO.recipientTaxId(), is(invoiceForm.recipientTaxId()));
    assertThat(invoiceDTO.items().size(), is(invoiceForm.items().size()));
    assertThat(invoiceDTO.items().get(0).name(), is(productResponse.name()));
    assertThat(invoiceDTO.status(), is(Status.APPROVED));
  }
  
  @Test
  public void whenAnErrorIsReturnedInTheExternalApiThenAnExceptionMustBeThrown() throws ApiException {
    // Given
    var invoiceForm = ModelUtil.getInvoiceForm();
    
    // When
    when(this.invoiceRepository.findByIssuerTaxId(invoiceForm.issuerTaxId()))
        .thenReturn(Collections.emptyList());
    when(this.logiStockClient.getProductByBarcode(any()))
        .thenThrow(ApiException.class);
    
    // Then
    assertThrows(ApiException.class, () -> this.invoiceService.create(invoiceForm));
  }
  
  // FindAll
  @Test
  public void shouldReturnAnInvoiceList() {
    // Given
    var invoiceFilter = ModelUtil.getInvoiceFilter();
    var invoice       = ModelUtil.getInvoice();
    
    var pageable = PageRequest.of(invoiceFilter.pageNumber(), invoiceFilter.pageSize());
    
    // When
    when(this.invoiceRepository.listAllByFilters(ISSUER_TAX_ID, invoiceFilter.statusList(),
        invoiceFilter.search(), invoiceFilter.startDate(), invoiceFilter.endDate(), pageable))
        .thenReturn(new PageImpl<>(Collections.singletonList(invoice)));
    
    // Then
    var invoiceList = this.invoiceService.listAll(ISSUER_TAX_ID, invoiceFilter);
    
    assertThat(invoiceList, is(not(empty())));
    assertThat(invoiceList.get(0).id(), is(invoice.getId()));
  }
  
  @Test
  public void shouldReturnAnEmptyInvoiceList() {
    // Given
    var invoiceFilter = ModelUtil.getInvoiceFilter();
    
    var pageable = PageRequest.of(invoiceFilter.pageNumber(), invoiceFilter.pageSize());
    
    // When
    when(this.invoiceRepository.listAllByFilters(ISSUER_TAX_ID, invoiceFilter.statusList(),
        invoiceFilter.search(), invoiceFilter.startDate(), invoiceFilter.endDate(), pageable))
        .thenReturn(new PageImpl<>(Collections.emptyList()));
    
    // Then
    var invoiceList = this.invoiceService.listAll(ISSUER_TAX_ID, invoiceFilter);
    
    assertThat(invoiceList, is(empty()));
  }
  
  // FindById
  @Test
  public void whenAValidInvoiceIdIsInformedThenAnInvoiceMustBeReturned() throws InvoiceNotFoundException {
    // Given
    var invoice = ModelUtil.getInvoice();
    
    // When
    when(this.invoiceRepository.findByIdAndIssuerTaxId(INVOICE_ID, ISSUER_TAX_ID))
        .thenReturn(Optional.of(invoice));
    
    // Then
    var invoiceDTO = this.invoiceService.findById(ISSUER_TAX_ID, INVOICE_ID);
    
    assertThat(invoiceDTO.id(), is(invoice.getId()));
    assertThat(invoiceDTO.accessKey(), is(invoice.getAccessKey()));
  }
  
  @Test
  public void whenAnInValidInvoiceIdIsInformedThenAnExceptionMustBeReturned() {
    // When
    when(this.invoiceRepository.findByIdAndIssuerTaxId(INVALID_INVOICE_ID, ISSUER_TAX_ID))
        .thenReturn(Optional.empty());
    
    // Then
    assertThrows(InvoiceNotFoundException.class,
        () -> this.invoiceService.findById(ISSUER_TAX_ID, INVALID_INVOICE_ID));
  }
  
  // Cancel
  @Test
  public void whenAValidInvoiceIdIsInformedToCancelInvoiceThenACanceledInvoiceMustBeReturned() throws InvoiceNotFoundException {
    // Given
    var invoice = ModelUtil.getInvoice();
    
    var invoiceCaptured = ArgumentCaptor.forClass(Invoice.class);
    
    // When
    when(this.invoiceRepository.findByIdAndIssuerTaxId(INVOICE_ID, ISSUER_TAX_ID))
        .thenReturn(Optional.of(invoice));
    when(this.invoiceRepository.save(invoiceCaptured.capture()))
        .thenAnswer(invocation -> invocation.getArgument(0));
    
    // Then
    var invoiceDTO = this.invoiceService.cancelInvoice(ISSUER_TAX_ID, INVOICE_ID);
    
    assertThat(invoiceDTO.id(), is(invoice.getId()));
    assertThat(invoiceDTO.accessKey(), is(invoice.getAccessKey()));
    assertThat(invoiceDTO.status(), is(Status.CANCELLED));
  }
  
  @Test
  public void whenAnInValidInvoiceIdIsInformedToCancelInvoiceThenAnExceptionMustBeReturned() {
    // When
    when(this.invoiceRepository.findByIdAndIssuerTaxId(INVOICE_ID, ISSUER_TAX_ID))
        .thenReturn(Optional.empty());
    
    // Then
    assertThrows(InvoiceNotFoundException.class,
        () -> this.invoiceService.cancelInvoice(ISSUER_TAX_ID, INVOICE_ID));
  }
  
}
