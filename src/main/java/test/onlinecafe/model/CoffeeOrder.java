package test.onlinecafe.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CoffeeOrder extends BaseEntity {
    private LocalDateTime orderDate = LocalDateTime.now();
    private String name;
    private String deliveryAddress;
    private List<CoffeeOrderItem> orderItems;
    private double cost;

    public CoffeeOrder() {
    }

    public CoffeeOrder(LocalDateTime orderDate, String name, String deliveryAddress, Double cost) {
        this(null, orderDate, name, deliveryAddress, cost);
    }

    public CoffeeOrder(Integer id, LocalDateTime orderDate, String name, String deliveryAddress, Double cost) {
        super(id);
        this.orderDate = orderDate;
        this.name = name;
        this.deliveryAddress = deliveryAddress;
        this.orderItems = new ArrayList<>();
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

    public void addOrderItem(CoffeeOrderItem orderItem) {
        this.orderItems.add(orderItem);
    }

    public void removeOrderItem(CoffeeOrderItem orderItem) {
        this.orderItems.remove(orderItem);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CoffeeOrder that = (CoffeeOrder) o;

        if (Double.compare(that.cost, cost) != 0) return false;
        if (orderDate != null ? !orderDate.equals(that.orderDate) : that.orderDate != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (deliveryAddress != null ? !deliveryAddress.equals(that.deliveryAddress) : that.deliveryAddress != null)
            return false;
        return orderItems != null ? orderItems.equals(that.orderItems) : that.orderItems == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        result = 31 * result + (orderDate != null ? orderDate.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (deliveryAddress != null ? deliveryAddress.hashCode() : 0);
        result = 31 * result + (orderItems != null ? orderItems.hashCode() : 0);
        temp = Double.doubleToLongBits(cost);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
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
