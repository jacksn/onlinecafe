package test.onlinecafe.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "CoffeeOrderItem")
public class CoffeeOrderItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id", nullable = false)
    private CoffeeType coffeeType;

    @NotNull
    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private CoffeeOrder order;

    public CoffeeOrderItem() {
    }

    public CoffeeOrderItem(CoffeeType type, Integer quantity) {
        this(null, type, quantity);
    }

    public CoffeeOrderItem(Integer id, CoffeeType type, Integer quantity) {
        super(id);
        this.coffeeType = type;
        this.quantity = quantity;
    }

    public CoffeeOrder getOrder() {
        return order;
    }

    public void setOrder(CoffeeOrder order) {
        this.order = order;
    }

    public CoffeeType getCoffeeType() {
        return coffeeType;
    }

    public void setCoffeeType(CoffeeType type) {
        this.coffeeType = type;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CoffeeOrderItem that = (CoffeeOrderItem) o;

        if (coffeeType != null ? !coffeeType.equals(that.coffeeType) : that.coffeeType != null) return false;
        return quantity != null ? quantity.equals(that.quantity) : that.quantity == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (coffeeType != null ? coffeeType.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CoffeeOrderItem{" +
                "id=" + getId() +
                ", coffeeType=" + coffeeType +
                ", quantity=" + quantity +
                '}';
    }
}
