package com.example.demo.service;

import com.example.demo.model.Cart;
import com.example.demo.model.Item;
import com.example.demo.repository.CartMapper;
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
public class CartService {

    private final ItemMapper ItemMapper;
    private final CartMapper CartMapper;


    //장바구니 중복상품 탐색
    public Cart searchItem(Long id, Long memberNo){
        Map<String, Long> params = new HashMap<>();
        params.put("id",id);
        params.put("memberNo",memberNo);
        return CartMapper.searchItem(params);
    }

    //장바구니 항목 탐색
    public Cart searchCart(Long id){
        return CartMapper.searchCart(id);
    }

    //장바구니 목록 조회
    public List<Cart> selectCartList(Long memberNo){
        return CartMapper.selectCartList(memberNo);
    }

    //장바구니 수량 변경
    public void updateCart(Cart cartItem){
        CartMapper.updateCart(cartItem);
    }

    //장바구니에 상품 추가
    public void addToCart(Cart newCartItem){
        CartMapper.insertCart(newCartItem);
    }

    //장바구니 항목 삭제
    public void deleteCart(Long cartId){
        CartMapper.deleteCart(cartId);
    }
}
