package test.onlinecafe.web;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import test.onlinecafe.CoffeeTypeTestData;
import test.onlinecafe.dto.CoffeeTypeDtoList;
import test.onlinecafe.dto.Notification;
import test.onlinecafe.dto.NotificationType;
import test.onlinecafe.service.CoffeeOrderService;
import test.onlinecafe.service.CoffeeTypeService;
import test.onlinecafe.service.ConfigurationService;
import test.onlinecafe.util.CoffeeOrderUtil;
import test.onlinecafe.util.discount.MockDiscount;

import static org.hamcrest.CoreMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static test.onlinecafe.CoffeeTypeTestData.COFFEE_TYPES_ENABLED;
import static test.onlinecafe.web.CoffeeController.*;

public class CoffeeControllerTest extends AbstractControllerTest {
    @InjectMocks
    private CoffeeController coffeeController;

    @Mock
    private CoffeeOrderService orderService;
    @Mock
    private CoffeeTypeService typeService;
    @Mock
    private ConfigurationService configurationService;

    @Before
    public void postConstruct() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(coffeeController)
                .build();
    }

    @Test
    public void getRootTest() throws Exception {
        when(typeService.getEnabled()).thenReturn(COFFEE_TYPES_ENABLED);
        Notification notification = new Notification(NotificationType.SUCCESS, "Success message");
        mockMvc.perform(get("/").sessionAttr(MODEL_ATTR_NOTIFICATION, notification))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute(MODEL_ATTR_COFFEE_TYPES, isA(CoffeeTypeDtoList.class)))
                .andExpect(model().attribute(MODEL_ATTR_DISCOUNT_DESCRIPTION, MockDiscount.DISCOUNT_DESCRIPTION))
                .andExpect(model().attribute(MODEL_ATTR_NOTIFICATION, notification));
        verify(typeService).getEnabled();
        verifyNoMoreInteractions(typeService);
    }

    @Test
    public void postRootTest() throws Exception {
        CoffeeTypeDtoList typeDtoList = CoffeeOrderUtil.getCoffeeTypeDtoList(CoffeeTypeTestData.COFFEE_TYPES_ENABLED);

    }
}
