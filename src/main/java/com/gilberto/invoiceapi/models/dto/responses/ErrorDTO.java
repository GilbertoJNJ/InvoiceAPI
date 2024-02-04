package com.gilberto.invoiceapi.models.dto.responses;

public record ErrorDTO(
    int code,
    String message
) {

}
