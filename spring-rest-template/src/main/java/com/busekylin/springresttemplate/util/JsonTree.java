package com.busekylin.springresttemplate.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class JsonTree {
    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
//构建 ObjectNode
        ObjectNode personNode = mapper.createObjectNode();
//添加/更改属性
        personNode.put("name", "中文");
        personNode.put("age", 40);

        ObjectNode addressNode = mapper.createObjectNode();
        addressNode.put("zip", "000000");
        addressNode.put("street", "Road NanJing");
//设置子节点
        personNode.set("address", addressNode);

        System.out.println(personNode.toString());
//读取 json
        JsonNode rootNode = mapper.readTree(personNode.toString());
        System.out.println(rootNode.get("name").asText());
    }
}
