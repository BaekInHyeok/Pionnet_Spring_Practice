package com.example.demo.repository;

import com.example.demo.model.Item;
import org.apache.ibatis.annotations.Mapper;
import com.example.demo.model.Cart;

import java.util.List;
import java.util.Map;

@Mapper
public interface ItemMapper {

    // 상품리스트 조회
    List<Item> selectItemList();

    // 상품 등록
    void saveItem(Item item);

}
