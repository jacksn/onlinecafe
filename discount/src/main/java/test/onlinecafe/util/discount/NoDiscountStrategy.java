package test.onlinecafe.util.discount;

import test.onlinecafe.model.ConfigurationItem;
import test.onlinecafe.repository.ConfigurationRepository;

public class NoDiscountStrategy implements DiscountStrategy {
    private ConfigurationRepository configurationRepository;

    // delivery cost
    private double m = 2;

    public NoDiscountStrategy(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    @Override
    public void init() {
        ConfigurationItem configurationItem = configurationRepository.get("m");
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
        return quantity * price;
    }

    @Override
    public double getDeliveryCost(double orderTotal) {
        return m;
    }
}
