package test.onlinecafe.model;

public class CoffeeType extends BaseEntity {

    private String typeName;
    private Double price;
    private Boolean disabled;

    public CoffeeType() {
    }

    public CoffeeType(String typeName, Double price, Boolean disabled) {
        this(null, typeName, price, disabled);
    }
    public CoffeeType(Integer id, String typeName, Double price, Boolean disabled) {
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
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

        if (typeName != null ? !typeName.equals(that.typeName) : that.typeName != null) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        return disabled != null ? disabled.equals(that.disabled) : that.disabled == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (typeName != null ? typeName.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (disabled != null ? disabled.hashCode() : 0);
        return result;
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
