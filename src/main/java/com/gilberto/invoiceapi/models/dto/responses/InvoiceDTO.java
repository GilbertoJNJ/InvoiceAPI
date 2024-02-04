package com.gilberto.invoiceapi.models.dto.responses;

import com.gilberto.invoiceapi.models.enums.Status;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record InvoiceDTO(
    Long id,
    String number,
    String accessKey,
    LocalDate issueDate,
    String recipientTaxId,
    String recipientName,
    List<InvoiceItemDTO> items,
    BigDecimal totalAmount,
    Status status,
    String notes
) {

}
