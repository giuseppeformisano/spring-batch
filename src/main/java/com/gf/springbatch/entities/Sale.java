package com.gf.springbatch.entities;

import jakarta.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(name = "Sale.findAll", query = "SELECT s FROM Sale s"),
        @NamedQuery(name = "Sale.findByType", query = "SELECT s FROM Sale s WHERE s.type = :type"),
        @NamedQuery(name = "Sale.getTotalSales", query = "SELECT SUM(s.quantity * s.unitPrice) FROM Sale s")
})
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String product;

    private String type;

    private int quantity;

    private double unitPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", product='" + product + '\'' +
                ", type='" + type + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                '}';
    }

}