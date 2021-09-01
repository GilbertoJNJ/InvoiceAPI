package com.gilberto.productsapi.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="produto")
public class Product {

    @Id
    private Long id;

    @Column(name="nome")
    private String name;

    //@ManyToOne
    //@JoinColumn(name = "categoria", referencedColumnName = "id")
    @Column(name="categoria_id")
    private Integer categoryId;

    @Column(name="preco_compra")
    private Double buyPrice;

    @Column(name="preco_venda")
    private Double sellPrice;
}
