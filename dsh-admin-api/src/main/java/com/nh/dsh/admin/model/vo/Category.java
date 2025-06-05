package com.nh.dsh.admin.model.vo;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private String code;
    private String name;
    private List<Category> children;

    public Category() {
    }

    public Category(String code, String name) {
        this.code = code;
        this.name = name;
        this.children = new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public List<Category> getChildren() {
        return children;
    }

    public void printCategory(int level) {
        String indent = "  ".repeat(level); // 使用两个空格进行缩进
        System.out.println(indent + code + ": " + name);
        for (Category child : children) {
            child.printCategory(level + 1);
        }
    }
}
