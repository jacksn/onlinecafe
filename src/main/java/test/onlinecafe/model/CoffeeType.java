package test.onlinecafe.model;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "coffeetype")
@NamedQueries({
        @NamedQuery(name = CoffeeType.GET_ALL, query = "SELECT ct FROM CoffeeType ct"),
        @NamedQuery(name = CoffeeType.GET_ENABLED, query = "SELECT ct FROM CoffeeType ct WHERE ct.disabled = false"),
        @NamedQuery(name = CoffeeType.DELETE, query = "DELETE FROM CoffeeType ct WHERE ct.id = :id"),
})
public class CoffeeType extends BaseEntity {
    public static final String GET_ALL = "CoffeeType.getAll";
    public static final String GET_ENABLED = "CoffeeType.getEnabled";
    public static final String DELETE = "CoffeeType.delete";

    @Column(name = "type_name")
    @NotNull
    @Size(min = 1, max = 200)
    @SafeHtml
    private String typeName;

    @Column(name = "price")
    @NotNull
    @Digits(integer = 10, fraction = 2)
    private BigDecimal price;

    @Column(name = "disabled")
    @Type(type = "yes_no")
    @NotNull
    private Boolean disabled;

    public CoffeeType() {
    }

    public CoffeeType(String typeName, BigDecimal price, Boolean disabled) {
        this(null, typeName, price, disabled);
    }

    public CoffeeType(Integer id, String typeName, BigDecimal price, Boolean disabled) {
        super(id);
        this.typeName = typeName;
        this.price = price;
        this.disabled = disabled;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CoffeeType that = (CoffeeType) o;
        return Objects.equals(typeName, that.typeName) &&
                Objects.equals(price, that.price) &&
                Objects.equals(disabled, that.disabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), typeName, price, disabled);
    }

    @Override
    public String toString() {
        return "CoffeeType{" +
                "id='" + getId() + '\'' +
                ", typeName='" + typeName + '\'' +
                ", price=" + price +
                ", disabled=" + disabled +
                '}';
    }
}
