package test.onlinecafe.to;

import java.util.List;

public class CoffeeOrderTo {
    private Integer id;
    private String name;
    private String deliveryAddress;
    private List<CoffeeOrderItemTo> orderItems;
    private double deliveryCost;
    private double cost;

    public CoffeeOrderTo(List<CoffeeOrderItemTo> orderItems, double deliveryCost, double cost) {
        this(null, null, null, orderItems, deliveryCost, cost);
    }

    public CoffeeOrderTo(String name, String deliveryAddress, List<CoffeeOrderItemTo> orderItems, double deliveryCost, double cost) {
        this(null, name, deliveryAddress, orderItems, deliveryCost, cost);
    }

    public CoffeeOrderTo(Integer id, String name, String deliveryAddress, List<CoffeeOrderItemTo> orderItems, double deliveryCost, double cost) {
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

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public List<CoffeeOrderItemTo> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<CoffeeOrderItemTo> orderItems) {
        this.orderItems = orderItems;
    }

    public double getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(double deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CoffeeOrderTo that = (CoffeeOrderTo) o;

        if (Double.compare(that.deliveryCost, deliveryCost) != 0) return false;
        if (Double.compare(that.cost, cost) != 0) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (deliveryAddress != null ? !deliveryAddress.equals(that.deliveryAddress) : that.deliveryAddress != null)
            return false;
        return orderItems != null ? orderItems.equals(that.orderItems) : that.orderItems == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (deliveryAddress != null ? deliveryAddress.hashCode() : 0);
        result = 31 * result + (orderItems != null ? orderItems.hashCode() : 0);
        temp = Double.doubleToLongBits(deliveryCost);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(cost);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "CoffeeOrderTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", orderItems=" + orderItems +
                ", deliveryCost=" + deliveryCost +
                ", cost=" + cost +
                '}';
    }
}
