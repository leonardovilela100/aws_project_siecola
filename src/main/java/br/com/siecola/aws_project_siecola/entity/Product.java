package br.com.siecola.aws_project_siecola.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"code"})
        }
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 32, nullable = false)
    private String name;


    @Column(length = 24, nullable = false)
    private String model;

    @Column(length = 8, nullable = false)
    private String code;

    @Column
    private float price;

    @Column(length = 12)
    private String color;

    public Product() {
    }

    public Product(long id, String name, String model, String code, float price, String color) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.code = code;
        this.price = price;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && Float.compare(price, product.price) == 0 && Objects.equals(name, product.name) && Objects.equals(model, product.model) && Objects.equals(code, product.code) && Objects.equals(color, product.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, model, code, price, color);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", model='" + model + '\'' +
                ", code='" + code + '\'' +
                ", price=" + price +
                ", color='" + color + '\'' +
                '}';
    }
}
