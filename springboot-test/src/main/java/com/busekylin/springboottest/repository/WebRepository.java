package com.busekylin.springboottest.repository;

import com.busekylin.springboottest.domain.Web;

public interface WebRepository {
    void addWeb(Web web);

    Web getWeb();
}
