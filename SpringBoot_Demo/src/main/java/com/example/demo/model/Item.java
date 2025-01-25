package com.example.demo.model;

import lombok.Data;

@Data
public class Item {
    private Long id;
    private String name;
    private double price;
    private int stock;
}