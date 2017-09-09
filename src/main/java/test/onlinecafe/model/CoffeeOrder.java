package test.onlinecafe.model;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateTimeConverter;

@Entity
@Table(name = "coffeeorder")
@NamedQueries({
        @NamedQuery(name = CoffeeOrder.GET_ALL, query = "SELECT co FROM CoffeeOrder co"),
        @NamedQuery(name = CoffeeOrder.DELETE, query = "DELETE FROM CoffeeOrder co WHERE co.id = :id"),
})
public class CoffeeOrder extends BaseEntity {
    public static final String GET_ALL = "CoffeeOrder.getAll";
    public static final String DELETE = "CoffeeOrder.delete";

    @Column(name = "order_date")
    @NotNull
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime orderDate = LocalDateTime.now();

    @NotNull
    @Column(name = "name")
    @Size(max = 100)
    private String name;

    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "delivery_address")
    private String deliveryAddress;

    @NotNull
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<CoffeeOrderItem> orderItems;

    @Column(name = "cost")
    @Digits(integer = 10, fraction = 2)
    @NotNull
    private BigDecimal cost;

    public CoffeeOrder() {
    }

    public CoffeeOrder(LocalDateTime orderDate, String name, String deliveryAddress, BigDecimal cost) {
        this(null, orderDate, name, deliveryAddress, cost);
    }

    public CoffeeOrder(Integer id, LocalDateTime orderDate, String name, String deliveryAddress, BigDecimal cost) {
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

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
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
        return Objects.equals(orderDate, that.orderDate) &&
                Objects.equals(name, that.name) &&
                Objects.equals(deliveryAddress, that.deliveryAddress) &&
                Objects.equals(orderItems, that.orderItems) &&
                Objects.equals(cost, that.cost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), orderDate, name, deliveryAddress, orderItems, cost);
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
