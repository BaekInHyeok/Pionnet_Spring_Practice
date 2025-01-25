package com.example.demo.model;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Cart {

    private Long id;
    private Long item_id;
    private Long member_no;
    private int quantity;
    private LocalDateTime created_at;

    private Item item;
}
