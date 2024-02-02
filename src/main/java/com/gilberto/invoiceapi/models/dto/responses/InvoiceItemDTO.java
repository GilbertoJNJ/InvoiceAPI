package com.gilberto.invoiceapi.models.dto.responses;

import java.math.BigDecimal;

public record InvoiceItemDTO(
    Long id,
    String name,
    String measureUnit,
    Integer quantity,
    BigDecimal unitPrice,
    BigDecimal totalPrice
) {

}
