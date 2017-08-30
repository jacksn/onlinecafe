package test.onlinecafe.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

public class CoffeeTypeDtoListWrapper {
    @NotNull
    private List<CoffeeTypeDto> coffeeTypeDtos;

    public List<CoffeeTypeDto> getCoffeeTypeDtos() {
        return coffeeTypeDtos;
    }

    public void setCoffeeTypeDtos(List<CoffeeTypeDto> coffeeTypeDtos) {
        this.coffeeTypeDtos = coffeeTypeDtos;
    }
}
