package com.busekylin.springboottest.service.impl;

import com.busekylin.springboottest.domain.Web;
import com.busekylin.springboottest.repository.WebRepository;
import com.busekylin.springboottest.service.WebService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultWebServiceImpl implements WebService {
    private final WebRepository webRepository;

    @Override
    public void addWeb(Web web) {
        webRepository.addWeb(web);
    }

    @Override
    public Web getWeb() {
        return webRepository.getWeb();
    }
}
