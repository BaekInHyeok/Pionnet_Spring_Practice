package com.example.demo.service;

import com.example.demo.model.Cart;
import com.example.demo.model.Item;
import com.example.demo.repository.ItemMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
@Slf4j
public class ItemService {

    private final ItemMapper ItemMapper;

    //상품목록 조회
    public List<Item> selectItemList() {
        return ItemMapper.selectItemList();
    }

    //상품등록
    public void saveItem(Item item) {
        ItemMapper.saveItem(item);
    }

}
