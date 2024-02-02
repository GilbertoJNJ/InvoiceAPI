package com.gilberto.invoiceapi.models.dto.requests;

import com.gilberto.invoiceapi.models.enums.Status;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record InvoiceFilter(
    @NotNull
    int pageSize,
    @NotNull
    int pageNumber,
    List<Status> statusList,
    String search,
    LocalDate startDate,
    LocalDate endDate
) {

}
