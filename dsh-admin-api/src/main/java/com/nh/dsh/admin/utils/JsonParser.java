package com.nh.dsh.admin.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nh.dsh.admin.model.vo.Category;
import org.w3c.dom.Node;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonParser {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<Category> parseJsonFile(String filePath) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(filePath)));
        return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, Category.class));
    }

}
