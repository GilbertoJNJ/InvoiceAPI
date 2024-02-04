package com.gilberto.invoiceapi.controllers;

import com.gilberto.invoiceapi.exceptions.ApiException;
import com.gilberto.invoiceapi.exceptions.InvoiceNotFoundException;
import com.gilberto.invoiceapi.models.dto.requests.InvoiceFilter;
import com.gilberto.invoiceapi.models.dto.requests.InvoiceForm;
import com.gilberto.invoiceapi.models.dto.responses.ErrorDTO;
import com.gilberto.invoiceapi.models.dto.responses.InvoiceDTO;
import com.gilberto.invoiceapi.services.IInvoiceService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/invoice")
public class InvoiceController {

    private final IInvoiceService invoiceService;
    
    @Autowired
    public InvoiceController(IInvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }
    
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid InvoiceForm invoiceForm) {
        try {
            return ResponseEntity.created(URI.create(""))
                .body(this.invoiceService.create(invoiceForm));
        } catch (ApiException exception) {
            return ResponseEntity.status(BAD_REQUEST)
                .body(new ErrorDTO(BAD_REQUEST.value(), exception.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<InvoiceDTO>> findAll(@RequestHeader String issuerTaxId,
                                                    @Valid InvoiceFilter invoiceFilter) {
        var invoices = this.invoiceService.listAll(issuerTaxId, invoiceFilter);
        return ResponseEntity.ok().body(invoices);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@RequestHeader String issuerTaxId,
                                               @PathVariable Long id) {
        try {
            var invoice = this.invoiceService.findById(issuerTaxId, id);
            return ResponseEntity.ok().body(invoice);
        } catch (InvoiceNotFoundException exception) {
            return ResponseEntity.status(NOT_FOUND)
                .body(new ErrorDTO(NOT_FOUND.value(), exception.getMessage()));
        }
    }
    
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Object> cancelInvoice(@RequestHeader String issuerTaxId,
                                                    @PathVariable Long id) {
        try {
            var invoice = this.invoiceService.cancelInvoice(issuerTaxId, id);
            return ResponseEntity.ok().body(invoice);
        } catch (InvoiceNotFoundException exception) {
            return ResponseEntity.status(NOT_FOUND)
                .body(new ErrorDTO(NOT_FOUND.value(), exception.getMessage()));
        }
    }

}
