package test.onlinecafe.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class CoffeeOrderDto {
    private Integer id;
    private String name;
    private String deliveryAddress;
    private List<CoffeeOrderItemDto> orderItems;
    private BigDecimal deliveryCost;
    private BigDecimal cost;

    public CoffeeOrderDto() {
    }

    public CoffeeOrderDto(List<CoffeeOrderItemDto> orderItems, BigDecimal deliveryCost, BigDecimal cost) {
        this(null, null, null, orderItems, deliveryCost, cost);
    }

    public CoffeeOrderDto(String name, String deliveryAddress, List<CoffeeOrderItemDto> orderItems, BigDecimal deliveryCost, BigDecimal cost) {
        this(null, name, deliveryAddress, orderItems, deliveryCost, cost);
    }

    public CoffeeOrderDto(Integer id, String name, String deliveryAddress, List<CoffeeOrderItemDto> orderItems, BigDecimal deliveryCost, BigDecimal cost) {
        this.id = id;
        this.name = name;
        this.deliveryAddress = deliveryAddress;
        this.orderItems = orderItems;
        this.deliveryCost = deliveryCost;
        this.cost = cost;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public List<CoffeeOrderItemDto> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<CoffeeOrderItemDto> orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(BigDecimal deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoffeeOrderDto that = (CoffeeOrderDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(deliveryAddress, that.deliveryAddress) &&
                Objects.equals(orderItems, that.orderItems) &&
                Objects.equals(deliveryCost, that.deliveryCost) &&
                Objects.equals(cost, that.cost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, deliveryAddress, orderItems, deliveryCost, cost);
    }

    @Override
    public String toString() {
        return "CoffeeOrderDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", orderItems=" + orderItems +
                ", deliveryCost=" + deliveryCost +
                ", cost=" + cost +
                '}';
    }
}
