package com.busekylin.springboottest;

import com.busekylin.springboottest.domain.Web;
import com.busekylin.springboottest.service.WebService;
import com.busekylin.springboottest.web.WebController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 参考 https://spring.io/guides/gs/testing-web/
 */
@RunWith(SpringRunner.class)
@WebMvcTest(WebController.class)
public class ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WebService webService;

    @Test
    public void controllerTest() throws Exception {
        when(webService.getWeb()).thenReturn(new Web("http://127.0.0.1", 8080));
        this.mockMvc.perform(get("/web")).andDo(print()).andExpect(status().isOk());
    }

}
