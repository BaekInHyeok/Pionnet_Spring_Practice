package com.example.demo.service;

import com.example.demo.model.UserInfo;
import com.example.demo.repository.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Member;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
@Slf4j
public class MemberService {

    private final MemberMapper MemberMapper;

    //로그인체크
    public String checkMemberIdPw(String id){
        return MemberMapper.checkMemberIdPw(id);
    };

    //사용자 정보 가져오기
    public UserInfo getUserInfo(String id){
        return MemberMapper.getUserInfo(id);
    }

}
