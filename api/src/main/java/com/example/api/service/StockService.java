package com.example.api.service;

import com.example.api.model.Stock;
import com.example.api.repository.StockMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
@Slf4j
public class StockService {
    private final StockMapper StockMapper;

    //상품 재고 정보 저장
    public void saveStock(Stock stock){
        StockMapper.saveStock(stock);
    }

    // 상품 ID로 재고 정보 조회
    public Integer getStockByItemId(Long itemId) {
        return StockMapper.getStockByItemId(itemId);
    }


}
