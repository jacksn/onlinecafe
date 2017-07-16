package test.onlinecafe.model;

import java.time.LocalDateTime;
import java.util.List;

public class CoffeeOrder extends BaseEntity {
    private LocalDateTime orderDate = LocalDateTime.now();
    private String name;
    private String deliveryAddress;
    private List<CoffeeOrderItem> orderItems;
    private Double cost;

    public CoffeeOrder() {
    }

    public CoffeeOrder(LocalDateTime orderDate, String name, String deliveryAddress) {
        this(null, orderDate, name, deliveryAddress, null, null);
    }

    public CoffeeOrder(LocalDateTime orderDate, String name, String deliveryAddress, List<CoffeeOrderItem> orderItems, Double cost) {
        this(null, orderDate, name, deliveryAddress, orderItems, cost);
    }

    public CoffeeOrder(Integer id, LocalDateTime orderDate, String name, String deliveryAddress, List<CoffeeOrderItem> orderItems, Double cost) {
        super(id);
        this.orderDate = orderDate;
        this.name = name;
        this.deliveryAddress = deliveryAddress;
        this.orderItems = orderItems;
        this.cost = cost;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public List<CoffeeOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<CoffeeOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CoffeeOrder that = (CoffeeOrder) o;

        if (orderDate != null ? !orderDate.equals(that.orderDate) : that.orderDate != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (deliveryAddress != null ? !deliveryAddress.equals(that.deliveryAddress) : that.deliveryAddress != null)
            return false;
        if (orderItems != null ? !orderItems.equals(that.orderItems) : that.orderItems != null) return false;
        return cost != null ? cost.equals(that.cost) : that.cost == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (orderDate != null ? orderDate.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (deliveryAddress != null ? deliveryAddress.hashCode() : 0);
        result = 31 * result + (orderItems != null ? orderItems.hashCode() : 0);
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CoffeeOrder{" +
                "id=" + getId() +
                ", orderDate=" + orderDate +
                ", name='" + name + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", orderItems=" + orderItems +
                ", cost=" + cost +
                '}';
    }
}
