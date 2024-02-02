package com.gilberto.invoiceapi.models.dto.requests;

import jakarta.validation.constraints.NotNull;

public record ItemForm(
    @NotNull
    String barcode,
    
    @NotNull
    Integer quantity
) {

}
