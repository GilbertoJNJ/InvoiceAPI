package com.gilberto.invoiceapi.utils;

import com.gilberto.invoiceapi.models.dto.requests.InvoiceFilter;
import com.gilberto.invoiceapi.models.dto.requests.InvoiceForm;
import com.gilberto.invoiceapi.models.dto.requests.ItemForm;
import com.gilberto.invoiceapi.models.dto.responses.InvoiceDTO;
import com.gilberto.invoiceapi.models.entity.Invoice;
import com.gilberto.invoiceapi.models.entity.InvoiceItem;
import com.gilberto.invoiceapi.models.enums.MeasureUnit;
import com.gilberto.invoiceapi.models.enums.Status;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

public class ModelUtil {
  
  public static InvoiceForm getInvoiceForm() {
    var items = Arrays.asList(
        new ItemForm("barcode1", 10),
        new ItemForm("barcode2", 12)
    );
    return new InvoiceForm(
        "issuerTaxId",
        "issuerName",
        "recipientTaxId",
        "recipientName",
        items,
        "notes"
    );
  }
  
  public static Invoice getInvoice() {
    return Invoice.builder()
        .id(1L)
        .number("number")
        .accessKey(UUID.randomUUID().toString())
        .issueDate(LocalDate.now())
        .issuerTaxId("issuerTaxId")
        .issuerName("issuerName")
        .recipientTaxId("recipientTaxId")
        .recipientName("recipientName")
        .totalAmount(BigDecimal.TEN)
        .status(Status.APPROVED)
        .notes("notes")
        .build();
  }
  
  public static InvoiceDTO getInvoiceDTO() {
    return new InvoiceDTO(
        1L,
        "number",
        UUID.randomUUID().toString(),
        LocalDate.now(),
        "recipientTaxId",
        "recipientName",
        null,
        BigDecimal.TEN,
        Status.APPROVED,
        "notes"
        );
  }
  
  public static InvoiceFilter getInvoiceFilter() {
    return new InvoiceFilter(
        10,
        0,
        Arrays.asList(Status.APPROVED, Status.CANCELLED),
        "search",
        LocalDate.of(2024, 01, 01),
        LocalDate.of(2024, 01, 31)
    );
  }
  
}
