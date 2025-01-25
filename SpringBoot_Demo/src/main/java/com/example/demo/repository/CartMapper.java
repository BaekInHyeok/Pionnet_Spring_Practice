package com.example.demo.repository;

import com.example.demo.model.Cart;
import com.example.demo.model.Item;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CartMapper {
    
    // 장바구니 리스트 조회
    List<Cart> selectCartList(Long memberNo);

    // 장바구니 중복 상품 조회
    Cart searchItem(Map<String,Long> params);

    // 장바구니 id로 조회
    Cart searchCart(Long id);

    // 장바구니 목록에 새 상품 추가
    void insertCart(Cart newCartItem);

    // 장바구니 상품 수량 증가
    void updateCart(Cart cartItem);

    // 장바구니 항목 삭제
    void deleteCart(Long id);
}
