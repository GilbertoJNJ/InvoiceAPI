package com.gilberto.invoiceapi.models.entity;

import com.gilberto.invoiceapi.models.enums.MeasureUnit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "ini_invoice_item")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItem {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ini_id")
  private Long id;
  
  @ManyToOne
  @JoinColumn(name = "ini_invoice_id", nullable = false)
  private Invoice invoice;
  
  @Column(name = "ini_name", nullable = false)
  private String name;
  
  @Column(name = "ini_measure_unit", nullable = false)
  @Enumerated(value = EnumType.STRING)
  private MeasureUnit measureUnit;
  
  @Column(name = "ini_quantity", nullable = false)
  private Integer quantity;
  
  @Column(name = "ini_unit_price", nullable = false)
  private BigDecimal unitPrice;
  
  public BigDecimal getTotalPrice() {
    return this.unitPrice.multiply(BigDecimal.valueOf(this.quantity));
  }
  
}
