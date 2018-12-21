package com.busekylin.springresttemplate.entity;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class Template {
    private String type;
    private String version;
    private List<String> lists;
    private Inner inner;
}
