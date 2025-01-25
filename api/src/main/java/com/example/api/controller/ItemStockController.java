package com.example.api.controller;

import com.example.api.model.Stock;
import com.example.api.model.StockInfo;
import com.example.api.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class ItemStockController {

    private final StockService stockService;

    @PostMapping("/getItemStock")
    public void getItemStock(@RequestBody StockInfo stockInfo){
        log.info("상품 스톡 정보 수신 : ID={}, STOCK={}",stockInfo.getId(), stockInfo.getStock());
        Stock stock = new Stock();
        stock.setItem_id(stockInfo.getId());
        stock.setQuantity(stockInfo.getStock());
        stock.setLast_updated(LocalDateTime.now());

        stockService.saveStock(stock);
    }

    @GetMapping("/sendItemStock/{itemId}")
    public Integer sendItemStock(@PathVariable Long itemId){
        try{
            Integer stock = stockService.getStockByItemId(itemId);
            if(stock != null){
                return stock;
            }else{
                log.warn("Stock Not Found");
                return 0;
            }
        } catch (Exception e){
            log.error("Error Fetching Stock");
            return 0;
        }
    }
    
    @GetMapping
    public void connectionCheck(){
        log.info("8080에서 접속 시도");
    }

}
