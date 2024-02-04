package com.gilberto.invoiceapi.models.enums;

import lombok.Getter;

@Getter
public enum MeasureUnit {
  KILOGRAM("Kg"),
  LITER("L"),
  PACK("Pct"),
  UNIT("Un");
  
  private final String label;
  
  MeasureUnit(String label) {
    this.label = label;
  }
  
}
