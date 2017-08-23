package test.onlinecafe.web;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ResourceControllerTest extends AbstractControllerTest {
    @Test
    public void testResources() throws Exception {
        mockMvc.perform(get("/css/style.css"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("navbar")));
    }
}
