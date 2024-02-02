package com.gilberto.invoiceapi.mappers;

public interface IMapper<T, U, V> {
  
  T toEntity(U form);
  
  V toDTO(T entity);
}
