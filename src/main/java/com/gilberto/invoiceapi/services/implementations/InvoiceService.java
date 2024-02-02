package com.gilberto.invoiceapi.services.implementations;

import com.gilberto.invoiceapi.clients.ILogiStockClient;
import com.gilberto.invoiceapi.exceptions.ApiException;
import com.gilberto.invoiceapi.exceptions.InvoiceNotFoundException;
import com.gilberto.invoiceapi.mappers.IMapper;
import com.gilberto.invoiceapi.mappers.InvoiceItemMapper;
import com.gilberto.invoiceapi.mappers.InvoiceMapper;
import com.gilberto.invoiceapi.models.dto.requests.InvoiceItemForm;
import com.gilberto.invoiceapi.models.entity.Invoice;
import com.gilberto.invoiceapi.models.entity.InvoiceItem;
import com.gilberto.invoiceapi.models.dto.requests.InvoiceFilter;
import com.gilberto.invoiceapi.models.dto.requests.InvoiceForm;
import com.gilberto.invoiceapi.models.dto.requests.ItemForm;
import com.gilberto.invoiceapi.models.dto.responses.InvoiceDTO;
import com.gilberto.invoiceapi.models.dto.responses.InvoiceItemDTO;
import com.gilberto.invoiceapi.models.dto.responses.ProductResponse;
import com.gilberto.invoiceapi.models.enums.Status;
import com.gilberto.invoiceapi.repositories.IInvoiceRepository;
import com.gilberto.invoiceapi.services.IInvoiceService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InvoiceService implements IInvoiceService {
  
  private final IInvoiceRepository invoiceRepository;
  
  private final ILogiStockClient logiStockClient;
  
  private final IMapper<Invoice, InvoiceForm, InvoiceDTO> invoiceMapper;
  
  private final IMapper<InvoiceItem, InvoiceItemForm, InvoiceItemDTO> invoiceItemMapper;
  
  @Autowired
  public InvoiceService(IInvoiceRepository invoiceRepository, ILogiStockClient logiStockClient) {
    this.invoiceRepository = invoiceRepository;
    this.logiStockClient   = logiStockClient;
    this.invoiceMapper     = new InvoiceMapper();
    this.invoiceItemMapper = new InvoiceItemMapper();
  }
  
  @Override
  @Transactional
  public InvoiceDTO create(InvoiceForm invoiceForm) throws ApiException {
    var number      = this.generateNextInvoiceNumber(invoiceForm.issuerTaxId());
    var accessKey   = this.generateInvoiceAccessKey();
    var items       = this.getItemList(invoiceForm.items());
    var totalAmount = this.calculateTotalAmount(items);
    
    var invoice = this.invoiceMapper.toEntity(invoiceForm);
    invoice.setNumber(number);
    invoice.setAccessKey(accessKey);
    invoice.setItems(items);
    invoice.setTotalAmount(totalAmount);
    invoice.setStatus(Status.APPROVED);
    
    items.forEach(item -> {
      item.setInvoice(invoice);
    });
    
    var savedInvoice = this.invoiceRepository.save(invoice);
    
    return this.invoiceMapper.toDTO(savedInvoice);
  }
  
  @Override
  public List<InvoiceDTO> listAll(String issuerTaxId, InvoiceFilter invoiceFilter) {
    var statusList = invoiceFilter.statusList() == null ?
                     Arrays.asList(Status.values()) : invoiceFilter.statusList();
    
    var search = invoiceFilter.search() == null ?
                 "" : invoiceFilter.search();
    
    var startDate = invoiceFilter.startDate() == null ?
                    LocalDate.now() : invoiceFilter.startDate();
    
    var endDate = invoiceFilter.endDate() == null ?
                  LocalDate.now() : invoiceFilter.endDate();
    
    var pageble = PageRequest.of(invoiceFilter.pageNumber(), invoiceFilter.pageSize());
    
    return this.invoiceRepository.listAllByFilters(issuerTaxId, statusList, search, startDate,
            endDate, pageble).stream()
        .map(this.invoiceMapper::toDTO)
        .collect(Collectors.toList());
  }
  
  @Override
  public InvoiceDTO listById(String issuerTaxId, Long id) throws InvoiceNotFoundException {
    return this.invoiceRepository.findByIdAndIssuerTaxId(id, issuerTaxId)
        .map(this.invoiceMapper::toDTO)
        .orElseThrow(InvoiceNotFoundException::new);
  }
  
  @Override
  public InvoiceDTO cancelInvoice(String issuerTaxId, Long id) throws InvoiceNotFoundException {
    var invoice = this.invoiceRepository.findByIdAndIssuerTaxId(id, issuerTaxId)
        .orElseThrow(InvoiceNotFoundException::new);
    invoice.setStatus(Status.CANCELLED);
    var savedInvoice = this.invoiceRepository.save(invoice);
    return this.invoiceMapper.toDTO(savedInvoice);
  }
  
  private String generateNextInvoiceNumber(String issuerTaxId) {
    var lastInvoiceNumber = getLastInvoiceNumber(issuerTaxId);
    var nextNumber = (lastInvoiceNumber == null)
                     ? 1
                     : Integer.parseInt(lastInvoiceNumber.substring(3)) + 1;
    
    return String.format("NF-%06d", nextNumber);
  }
  
  private String getLastInvoiceNumber(String issuerTaxId) {
    return this.invoiceRepository.findByIssuerTaxId(issuerTaxId).stream()
        .map(Invoice::getNumber)
        .max(Comparator.naturalOrder())
        .orElse(null);
  }
  
  private String generateInvoiceAccessKey() {
    return UUID.randomUUID().toString();
  }
  
  private List<InvoiceItem> getItemList(List<ItemForm> items) throws ApiException {
    var invoiceItemList = new ArrayList<InvoiceItemForm>();
    for (ItemForm itemForm : items) {
      var response = this.logiStockClient.getProductByBarcode(itemForm.barcode());
      
      this.logiStockClient.decreaseProductStock(response.id(), itemForm.quantity());
      
      invoiceItemList.add(generateInvoiceItemForm(itemForm, response));
    }
    
    return invoiceItemList.stream()
        .map(this.invoiceItemMapper::toEntity)
        .collect(Collectors.toList());
  }
  
  private static InvoiceItemForm generateInvoiceItemForm(ItemForm itemForm, ProductResponse response) {
    return new InvoiceItemForm(
        response.name(),
        response.measureUnit(),
        itemForm.quantity(),
        response.unitPrice()
    );
  }
  
  public BigDecimal calculateTotalAmount(List<InvoiceItem> items) {
    return items.stream()
        .map(InvoiceItem::getTotalPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
  
}
