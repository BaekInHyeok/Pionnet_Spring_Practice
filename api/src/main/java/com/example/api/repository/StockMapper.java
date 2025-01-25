package com.example.api.repository;

import org.apache.ibatis.annotations.Mapper;
import com.example.api.model.Stock;

@Mapper
public interface StockMapper {

    //등록하는 상품에 대한 상품ID와 상품재고 등록
    void saveStock(Stock stock);

    //상품ID별 재고량 조회
    Integer getStockByItemId(Long itemId);


}
