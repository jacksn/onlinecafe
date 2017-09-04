package test.onlinecafe.web;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import test.onlinecafe.CoffeeOrderTestData;
import test.onlinecafe.dto.CoffeeTypeDtoListWrapper;
import test.onlinecafe.dto.Notification;
import test.onlinecafe.dto.NotificationType;
import test.onlinecafe.model.CoffeeOrder;
import test.onlinecafe.model.CoffeeOrderItem;
import test.onlinecafe.model.CoffeeType;
import test.onlinecafe.service.CoffeeOrderService;
import test.onlinecafe.service.CoffeeTypeService;
import test.onlinecafe.service.ConfigurationService;
import test.onlinecafe.util.discount.MockDiscount;

import static org.hamcrest.CoreMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static test.onlinecafe.CoffeeTypeTestData.COFFEE_TYPES_ENABLED;
import static test.onlinecafe.web.CoffeeController.*;

public class CoffeeControllerTest extends AbstractControllerTest {
    @InjectMocks
    private CoffeeController coffeeController;

    @Mock
    private MessageSource messageSource;
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
                .andExpect(model().attribute(MODEL_ATTR_COFFEE_TYPES, isA(CoffeeTypeDtoListWrapper.class)))
                .andExpect(model().attribute(MODEL_ATTR_DISCOUNT_DESCRIPTION, MockDiscount.DISCOUNT_DESCRIPTION))
                .andExpect(model().attribute(MODEL_ATTR_NOTIFICATION, notification));
        verify(typeService).getEnabled();
        verifyNoMoreInteractions(typeService);
    }

    @Test
    public void postValidDataToRootTest() throws Exception {
        CoffeeOrder order = CoffeeOrderTestData.getCoffeeOrder2();
        for (CoffeeOrderItem orderItem : order.getOrderItems()) {
            CoffeeType type = orderItem.getCoffeeType();
            when(typeService.get(type.getId())).thenReturn(type);
        }

        mockMvc.perform(
                post("/")
                        .param("content-type", "application/x-www-form-urlencoded")
                        .param("coffeeTypeDtos[0].typeId", "2")
                        .param("coffeeTypeDtos[0].selected", "true")
                        .param("coffeeTypeDtos[0].quantity", "2")
                        .param("coffeeTypeDtos[1].typeId", "4")
                        .param("coffeeTypeDtos[1].selected", "true")
                        .param("coffeeTypeDtos[1].quantity", "1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("order"));
    }

    @Test
    public void postListWithZeroQuantityToRootTest() throws Exception {
        CoffeeOrder order = CoffeeOrderTestData.getCoffeeOrder2();
        for (CoffeeOrderItem orderItem : order.getOrderItems()) {
            CoffeeType type = orderItem.getCoffeeType();
            when(typeService.get(type.getId())).thenReturn(type);
        }
        final String emptyOrder = "EMPTY ORDER";
        when(messageSource.getMessage(eq(MESSAGE_ERROR_EMPTY_ORDER), eq(null), any())).thenReturn(emptyOrder);
        mockMvc.perform(
                post("/")
                        .param("content-type", "application/x-www-form-urlencoded")
                        .param("coffeeTypeDtos[0].typeId", "2")
                        .param("coffeeTypeDtos[0].selected", "true")
                        .param("coffeeTypeDtos[0].quantity", "0")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute(MODEL_ATTR_NOTIFICATION, new Notification(NotificationType.ERROR, emptyOrder)));
    }

}
