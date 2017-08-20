package test.onlinecafe.dto;

import test.onlinecafe.model.CoffeeType;

import javax.validation.constraints.NotNull;

public class CoffeeTypeDto {
    private int typeId;
    private String typeName;
    private double price;

    private int quantity;
    private boolean selected;

    public CoffeeTypeDto() {
    }

    public CoffeeTypeDto(CoffeeType type) {
        this.typeId = type.getId();
        this.typeName = type.getTypeName();
        this.price = type.getPrice();
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
