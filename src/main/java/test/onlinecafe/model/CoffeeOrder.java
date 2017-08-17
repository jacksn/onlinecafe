package test.onlinecafe.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CoffeeOrder")
@NamedQueries({
        @NamedQuery(name = CoffeeOrder.GET_ALL, query = "SELECT co FROM CoffeeOrder co"),
        @NamedQuery(name = CoffeeOrder.DELETE, query = "DELETE FROM CoffeeOrder co WHERE co.id = :id"),
})
public class CoffeeOrder extends BaseEntity {
    public static final String GET_ALL = "CoffeeOrder.getAll";
    public static final String DELETE = "CoffeeOrder.delete";

    @Column(name = "order_date")
    private LocalDateTime orderDate = LocalDateTime.now();

    @Column(name = "name")
    private String name;

    @NotBlank
    @Column(name = "delivery_address")
    private String deliveryAddress;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<CoffeeOrderItem> orderItems;

    @NotNull
    @Column(name = "cost")
    private Double cost;

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

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public List<CoffeeOrderItem> getOrderItems() {
        return orderItems;
    }

    public void addOrderItem(CoffeeOrderItem orderItem) {
        orderItem.setOrder(this);
        this.orderItems.add(orderItem);
    }

    public void removeOrderItem(CoffeeOrderItem orderItem) {
        orderItem.setOrder(null);
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
