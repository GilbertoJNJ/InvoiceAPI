package com.gilberto.invoiceapi.models.dto.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gilberto.invoiceapi.models.enums.MeasureUnit;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductResponse(
    Long id,
    String name,
    BigDecimal unitPrice,
    MeasureUnit measureUnit
) {

}
