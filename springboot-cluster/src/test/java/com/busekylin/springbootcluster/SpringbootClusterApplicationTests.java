package com.busekylin.springbootcluster;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SpringbootClusterApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void personTest() throws Exception {
        this.mockMvc.perform(get("/person")).andDo(print());
    }

    @Test
    public void personsTest() throws Exception {
        this.mockMvc.perform(get("/persons/8")).andDo(print());
    }
}
