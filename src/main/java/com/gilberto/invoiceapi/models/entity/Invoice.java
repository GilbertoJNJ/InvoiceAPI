package com.gilberto.invoiceapi.models.entity;

import com.gilberto.invoiceapi.models.enums.Status;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity(name = "inv_invoice")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "inv_id")
  private Long id;
  
  @Column(name = "inv_number", nullable = false)
  private String number;
  
  @Column(name = "inv_access_key", nullable = false, unique = true)
  private String accessKey;
  
  @Column(name = "inv_issue_date", nullable = false)
  @CreationTimestamp
  private LocalDate issueDate;
  
  @Column(name = "inv_issuer_tax_id", nullable = false)
  private String issuerTaxId;
  
  @Column(name = "inv_issuer_name", nullable = false)
  private String issuerName;
  
  @Column(name = "inv_recipient_tax_id", nullable = false)
  private String recipientTaxId;
  
  @Column(name = "inv_recipient_name", nullable = false)
  private String recipientName;
  
  @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
  private List<InvoiceItem> items;
  
  @Column(name = "inv_total_amount")
  private BigDecimal totalAmount;
  
  @Column(name = "inv_status", nullable = false)
  @Enumerated(value = EnumType.STRING)
  private Status status;
  
  @Column(name = "inv_notes")
  private String notes;
  
}
