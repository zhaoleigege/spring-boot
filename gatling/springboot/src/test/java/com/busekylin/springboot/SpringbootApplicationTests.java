package com.busekylin.springboot;

import com.busekylin.web.WebController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(WebController.class)
public class SpringbootApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void personTest() throws Exception {
        mockMvc.perform(get("/persons"))
                .andExpect(status().isOk());
    }

    @Test
    public void classRoomTest() throws Exception {
        mockMvc.perform(get("/classRooms"))
                .andExpect(status().isOk());
    }
}

