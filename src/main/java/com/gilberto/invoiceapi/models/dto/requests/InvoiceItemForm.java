package com.gilberto.invoiceapi.models.dto.requests;

import com.gilberto.invoiceapi.models.enums.MeasureUnit;
import java.math.BigDecimal;

public record InvoiceItemForm(
    String name,
    MeasureUnit measureUnit,
    Integer quantity,
    BigDecimal unitPrice
) {

}
