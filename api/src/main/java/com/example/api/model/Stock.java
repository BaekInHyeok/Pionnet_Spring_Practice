package com.example.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class Stock {
    private Long id;
    private Long item_id;
    private int quantity;
    private LocalDateTime last_updated;
}
