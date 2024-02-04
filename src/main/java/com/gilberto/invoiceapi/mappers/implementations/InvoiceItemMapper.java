package com.gilberto.invoiceapi.mappers.implementations;

import com.gilberto.invoiceapi.mappers.IMapper;
import com.gilberto.invoiceapi.models.dto.requests.InvoiceItemForm;
import com.gilberto.invoiceapi.models.entity.InvoiceItem;
import com.gilberto.invoiceapi.models.dto.responses.InvoiceItemDTO;

public class InvoiceItemMapper implements IMapper<InvoiceItem, InvoiceItemForm, InvoiceItemDTO> {
  
  @Override
  public InvoiceItem toEntity(InvoiceItemForm form) {
    return InvoiceItem.builder()
        .name(form.name())
        .measureUnit(form.measureUnit())
        .unitPrice(form.unitPrice())
        .quantity(form.quantity())
        .build();
  }
  
  @Override
  public InvoiceItemDTO toDTO(InvoiceItem entity) {
    return new InvoiceItemDTO(
        entity.getId(),
        entity.getName(),
        entity.getMeasureUnit().getLabel(),
        entity.getQuantity(),
        entity.getUnitPrice(),
        entity.getTotalPrice()
    );
  }
  
}
