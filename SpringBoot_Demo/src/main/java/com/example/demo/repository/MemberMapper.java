package com.example.demo.repository;

import com.example.demo.model.Cart;
import com.example.demo.model.Item;
import com.example.demo.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {
    //사용자 비번
    String checkMemberIdPw(String id);

    //사용자정보 가져오기
    UserInfo getUserInfo(String id);
}
