package com.gilberto.storeapi.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "categoria")
public class Category {

    @Id
    private Long id;

    @Column(name = "nome")
    private String name;
}
