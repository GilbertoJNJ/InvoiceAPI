package com.gilberto.invoiceapi.mappers;

import com.gilberto.invoiceapi.models.dto.requests.InvoiceItemForm;
import com.gilberto.invoiceapi.models.entity.InvoiceItem;
import com.gilberto.invoiceapi.models.dto.responses.InvoiceItemDTO;

public class InvoiceItemMapper implements IMapper<InvoiceItem, InvoiceItemForm, InvoiceItemDTO>{
  
  @Override
  public InvoiceItem toEntity(InvoiceItemForm invoiceItemForm) {
    return InvoiceItem.builder()
        .name(invoiceItemForm.name())
        .measureUnit(invoiceItemForm.measureUnit())
        .unitPrice(invoiceItemForm.unitPrice())
        .quantity(invoiceItemForm.quantity())
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
