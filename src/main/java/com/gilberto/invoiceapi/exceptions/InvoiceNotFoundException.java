package com.gilberto.invoiceapi.exceptions;

public class InvoiceNotFoundException extends Exception {
  
  public InvoiceNotFoundException() {
    super("Invoice not found!");
  }
  
}
