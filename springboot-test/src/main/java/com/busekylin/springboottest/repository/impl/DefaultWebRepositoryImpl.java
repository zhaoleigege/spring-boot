package com.busekylin.springboottest.repository.impl;

import com.busekylin.springboottest.domain.Web;
import com.busekylin.springboottest.repository.WebRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DefaultWebRepositoryImpl implements WebRepository {
    private static final List<Web> webDatabase;

    static {
        webDatabase = new ArrayList<>();
    }

    @Override
    public void addWeb(Web web) {
        webDatabase.add(web);
    }

    @Override
    public Web getWeb() {
        return webDatabase.get((int) (Math.random() * webDatabase.size()));
    }
}
