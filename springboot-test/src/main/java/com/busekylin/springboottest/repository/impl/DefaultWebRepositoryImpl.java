package com.busekylin.springboottest.repository.impl;

import com.busekylin.springboottest.domain.Web;
import com.busekylin.springboottest.repository.WebRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class DefaultWebRepositoryImpl implements WebRepository {
    private static final List<Web> webDatabase;

    static {
        webDatabase = Stream.generate(() -> new Web("url", (int) (Math.random() * 1000))).limit(4).collect(Collectors.toList());
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
