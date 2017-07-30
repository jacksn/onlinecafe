package test.onlinecafe.to;

import test.onlinecafe.model.CoffeeType;

public class CoffeeOrderItemTo {
    private Integer id;
    private CoffeeType coffeeType;
    private Integer quantity;
    private double cost;
    private boolean discounted;

    public CoffeeOrderItemTo(CoffeeType type, Integer quantity, double cost, boolean discounted) {
        this(null, type, quantity, cost, discounted);
    }

    public CoffeeOrderItemTo(Integer id, CoffeeType coffeeType, Integer quantity, double cost, boolean discounted) {
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

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
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

        CoffeeOrderItemTo that = (CoffeeOrderItemTo) o;

        if (Double.compare(that.cost, cost) != 0) return false;
        if (discounted != that.discounted) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (coffeeType != null ? !coffeeType.equals(that.coffeeType) : that.coffeeType != null) return false;
        return quantity != null ? quantity.equals(that.quantity) : that.quantity == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (coffeeType != null ? coffeeType.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        temp = Double.doubleToLongBits(cost);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (discounted ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CoffeeOrderItemTo{" +
                "id=" + id +
                ", coffeeType=" + coffeeType +
                ", quantity=" + quantity +
                ", cost=" + cost +
                ", discounted=" + discounted +
                '}';
    }
}
