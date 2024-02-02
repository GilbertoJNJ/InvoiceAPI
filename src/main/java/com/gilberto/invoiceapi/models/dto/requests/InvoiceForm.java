package com.gilberto.invoiceapi.models.dto.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record InvoiceForm(
    @NotNull
    String issuerTaxId,
    
    @NotNull
    String issuerName,
    
    @NotNull
    String recipientTaxId,
    
    @NotNull
    String recipientName,
    
    @Valid
    List<ItemForm> items,
    
    String notes
) {

}
