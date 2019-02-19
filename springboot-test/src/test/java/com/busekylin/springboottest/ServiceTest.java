package com.busekylin.springboottest;

import com.busekylin.springboottest.domain.Web;
import com.busekylin.springboottest.repository.WebRepository;
import com.busekylin.springboottest.service.WebService;
import com.busekylin.springboottest.service.impl.DefaultWebServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DefaultWebServiceImpl.class})
public class ServiceTest {
    @Autowired
    private WebService webService;

    @MockBean
    private WebRepository webRepository;

    @Test
    public void serviceTest() {
        when(webRepository.getWeb()).thenReturn(new Web("http://localhost", 8080));
        System.out.println(webService.getWeb());
    }
}
