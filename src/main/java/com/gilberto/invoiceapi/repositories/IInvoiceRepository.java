package com.gilberto.invoiceapi.repositories;

import com.gilberto.invoiceapi.models.entity.Invoice;
import com.gilberto.invoiceapi.models.enums.Status;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IInvoiceRepository extends JpaRepository<Invoice, Long> {
  
  List<Invoice> findByIssuerTaxId(String issuerTaxId);
  
  @Query("select invoice from inv_invoice as invoice " +
      " where invoice.issuerTaxId = :issuerTaxId " +
      "   and (invoice.status in :statusList) " +
      "   and ((:search = '' or cast(invoice.id as text) = :search) " +
      "     or (upper(invoice.number) like concat('%', upper(:search), '%')) " +
      "     or (upper(invoice.accessKey) like concat('%', upper(:search), '%')) " +
      "     or (upper(invoice.issuerTaxId) like concat('%', upper(:search), '%')) " +
      "     or (upper(invoice.issuerName) like concat('%', upper(:search), '%'))) " +
      "   and invoice.issueDate between :startDate and :endDate " +
      " order by invoice.issueDate desc")
  Page<Invoice> listAllByFilters(@Param("issuerTaxId") String issuerTaxId,
                                 @Param("statusList") List<Status> statusList,
                                 @Param("search") String search,
                                 @Param("startDate") LocalDate startDate,
                                 @Param("endDate") LocalDate endDate,
                                 Pageable pageable);
  
  Optional<Invoice> findByIdAndIssuerTaxId(Long id, String issuerTaxId);
  
}
