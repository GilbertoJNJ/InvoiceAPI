package com.gilberto.invoiceapi.mappers.implementations;

import com.gilberto.invoiceapi.mappers.IMapper;
import com.gilberto.invoiceapi.models.entity.Invoice;
import com.gilberto.invoiceapi.models.dto.requests.InvoiceForm;
import com.gilberto.invoiceapi.models.dto.responses.InvoiceDTO;
import java.util.stream.Collectors;

public class InvoiceMapper implements IMapper<Invoice, InvoiceForm, InvoiceDTO> {
  
  private final InvoiceItemMapper invoiceItemMapper = new InvoiceItemMapper();
  
  @Override
  public Invoice toEntity(InvoiceForm form) {
    return Invoice.builder()
        .issuerTaxId(form.issuerTaxId())
        .issuerName(form.issuerName())
        .recipientTaxId(form.recipientTaxId())
        .recipientName(form.recipientName())
        .notes(form.notes())
        .build();
  }
  
  @Override
  public InvoiceDTO toDTO(Invoice entity) {
    var items = entity.getItems().stream()
        .map(this.invoiceItemMapper::toDTO)
        .collect(Collectors.toList());
    
    return new InvoiceDTO(
        entity.getId(),
        entity.getNumber(),
        entity.getAccessKey(),
        entity.getIssueDate(),
        entity.getRecipientTaxId(),
        entity.getRecipientName(),
        items,
        entity.getTotalAmount(),
        entity.getStatus(),
        entity.getNotes()
    );
  }
}
