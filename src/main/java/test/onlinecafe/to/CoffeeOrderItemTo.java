package test.onlinecafe.to;

import test.onlinecafe.model.CoffeeOrderItem;
import test.onlinecafe.model.CoffeeType;

public class CoffeeOrderItemTo extends CoffeeOrderItem {
    private double cost;

    public CoffeeOrderItemTo(CoffeeType type, Integer quantity, double cost) {
        super(type, quantity);
        this.cost = cost;
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
        if (!super.equals(o)) return false;

        CoffeeOrderItemTo that = (CoffeeOrderItemTo) o;

        return Double.compare(that.cost, cost) == 0;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(cost);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
