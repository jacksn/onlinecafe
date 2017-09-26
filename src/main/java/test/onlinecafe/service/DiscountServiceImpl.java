package test.onlinecafe.service;

import org.springframework.stereotype.Service;
import test.onlinecafe.service.discount.Discount;
import test.onlinecafe.util.exception.NotFoundException;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Map;

@Service
public class DiscountServiceImpl implements DiscountService {
    public static final String ACTIVE_DISCOUNT_CONFIG_KEY = "activeDiscount";
    private Map<String, Discount> discountMap;
    private Discount activeDiscount;
    private ConfigurationService configurationService;
    private String defaultDiscountName;
    private String activeDiscountName;

    public DiscountServiceImpl(Map<String, Discount> discountMap, ConfigurationService configurationService, String defaultDiscountName) {
        this.discountMap = discountMap;
        this.configurationService = configurationService;
        this.defaultDiscountName = defaultDiscountName;
    }

    @PostConstruct
    public void init() {
        try {
            this.activeDiscountName = configurationService.get(ACTIVE_DISCOUNT_CONFIG_KEY);
        } catch (NotFoundException e) {
            this.activeDiscountName = defaultDiscountName;
        }
        this.activeDiscount = discountMap.get(this.activeDiscountName);
    }

    @Override
    public Map<String, Discount> getDiscountMap() {
        return discountMap;
    }

    @Override
    public boolean setActiveDiscount(String discountName) {
        Discount discount = discountMap.get(discountName);
        if (discount != null) {
            this.activeDiscount = discount;
            this.activeDiscountName = discountName;
            configurationService.save(ACTIVE_DISCOUNT_CONFIG_KEY, discountName);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Discount getActiveDiscount() {
        return this.activeDiscount;
    }

    @Override
    public String getActiveDiscountName() {
        return this.activeDiscountName;
    }

    @Override
    public BigDecimal getDeliveryCost(BigDecimal orderTotalCost) {
        return this.activeDiscount.getDeliveryCost(orderTotalCost);
    }

    @Override
    public BigDecimal getDiscountedItemCost(int quantity, BigDecimal price) {
        return this.activeDiscount.getDiscountedItemCost(quantity, price);
    }
}
