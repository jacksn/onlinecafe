package test.onlinecafe.util.discount;

import test.onlinecafe.model.ConfigurationItem;
import test.onlinecafe.repository.ConfigurationRepository;

public class SimpleDiscountStrategy implements DiscountStrategy {
    private ConfigurationRepository configurationRepository;
    // n'th cup of one type if free
    private int n = 5;
    // if order total > x then delivery is free
    private double x = 10;
    // delivery cost
    private double m = 2;

    public SimpleDiscountStrategy(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    public void init() {
        ConfigurationItem configurationItem = configurationRepository.get("n");
        if (configurationItem != null) {
            try {
                this.n = Integer.parseInt(configurationItem.getValue());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        configurationItem = configurationRepository.get("x");
        if (configurationItem != null) {
            try {
                this.x = Double.parseDouble(configurationItem.getValue());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        configurationItem = configurationRepository.get("m");
        if (configurationItem != null) {
            try {
                this.m = Double.parseDouble(configurationItem.getValue());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public double getDiscountedItemCost(int quantity, double price) {
        return (quantity - quantity / n) * price;
    }

    @Override
    public double getDeliveryCost(double orderTotal) {
        return orderTotal > x ? 0 : m;
    }
}
