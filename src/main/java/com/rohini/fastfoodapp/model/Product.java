 package com.rohini.fastfoodapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.rohini.fastfoodapp.enumeration.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rohini
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idProduct;
    @NotNull(message = "name cannot be empty or null")
    @Column(length = 50)
    private String name;
    private int calories;
    @NotNull(message = "description cannot be empty or null")
    @Column(length = 500)
    private String description;
    @NotNull(message = "price cannot be empty or null")
    private int price;
    private String ImageUrl;
    private String duration;
    private int discountPoint;
    private int highlight;
    @ManyToOne
    @NotNull(message = "idCategory cannot be empty or null")
    @JoinColumn(name = "idCategory")
    private Category Category;
    
    private Status status;
}
