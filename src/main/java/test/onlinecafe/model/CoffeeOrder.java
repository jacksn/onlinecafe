package test.onlinecafe.model;

import java.time.LocalDateTime;
import java.util.List;

public class CoffeeOrder extends BaseEntity {
    private LocalDateTime orderDate = LocalDateTime.now();
    private String name;
    private String deliveryAddress;
    private List<CoffeeOrderItem> orderItems;
    private Double cost;

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
}
