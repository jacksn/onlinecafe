package test.onlinecafe.dto;

import test.onlinecafe.model.CoffeeType;

import java.math.BigDecimal;
import java.util.Objects;

public class CoffeeOrderItemDto {
    private Integer id;
    private CoffeeType coffeeType;
    private Integer quantity;
    private BigDecimal cost;
    private boolean discounted;

    public CoffeeOrderItemDto(CoffeeType type, Integer quantity, BigDecimal cost, boolean discounted) {
        this(null, type, quantity, cost, discounted);
    }

    public CoffeeOrderItemDto(Integer id, CoffeeType coffeeType, Integer quantity, BigDecimal cost, boolean discounted) {
        this.id = id;
        this.coffeeType = coffeeType;
        this.quantity = quantity;
        this.cost = cost;
        this.discounted = discounted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CoffeeType getCoffeeType() {
        return coffeeType;
    }

    public void setCoffeeType(CoffeeType coffeeType) {
        this.coffeeType = coffeeType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public boolean isDiscounted() {
        return discounted;
    }

    public void setDiscounted(boolean discounted) {
        this.discounted = discounted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoffeeOrderItemDto that = (CoffeeOrderItemDto) o;
        return discounted == that.discounted &&
                Objects.equals(id, that.id) &&
                Objects.equals(coffeeType, that.coffeeType) &&
                Objects.equals(quantity, that.quantity) &&
                Objects.equals(cost, that.cost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, coffeeType, quantity, cost, discounted);
    }

    @Override
    public String toString() {
        return "CoffeeOrderItemDto{" +
                "id=" + id +
                ", coffeeType=" + coffeeType +
                ", quantity=" + quantity +
                ", cost=" + cost +
                ", discounted=" + discounted +
                '}';
    }
}
