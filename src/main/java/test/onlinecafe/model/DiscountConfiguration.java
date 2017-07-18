package test.onlinecafe.model;

public class DiscountConfiguration {
    private Integer nthCup;
    private Double deliveryCost;
    private Double freeDeliveryThreshold;

    public DiscountConfiguration(Integer nthCup, Double deliveryCost, Double freeDeliveryThreshold) {
        this.nthCup = nthCup;
        this.deliveryCost = deliveryCost;
        this.freeDeliveryThreshold = freeDeliveryThreshold;
    }

    public Integer getNthCup() {
        return nthCup;
    }

    public void setNthCup(Integer nthCup) {
        this.nthCup = nthCup;
    }

    public Double getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(Double deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public Double getFreeDeliveryThreshold() {
        return freeDeliveryThreshold;
    }

    public void setFreeDeliveryThreshold(Double freeDeliveryThreshold) {
        this.freeDeliveryThreshold = freeDeliveryThreshold;
    }

    @Override
    public String toString() {
        return "DiscountConfiguration{" +
                "nthCup=" + nthCup +
                ", deliveryCost=" + deliveryCost +
                ", freeDeliveryThreshold=" + freeDeliveryThreshold +
                '}';
    }
}
