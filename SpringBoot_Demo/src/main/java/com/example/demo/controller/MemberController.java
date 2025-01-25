package com.example.demo.controller;

import com.example.demo.model.UserInfo;
import com.example.demo.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    
    //로그인 페이지
    @GetMapping("/login")
    public String loginPage(Model model){
        return "login_form";
    }

    //로그인
    @PostMapping("/login")
    public String userLogin(Model model, @RequestParam("memberId") String id, @RequestParam("password") String pw, HttpSession session, HttpServletResponse response){
        //id로 로그인하려는 회원의 pw 가져오기
        log.trace("id :{}",id);
        String password = memberService.checkMemberIdPw(id);
        
        //경우의 수 : null(회원 없음), 일치, 불일치
        //일치하면 로그인(쿠키/세션), 불일치하면 비번 불일치, null이면 해당 id로 가입한 회원이 없음
        if(password == null){//해당 id로 가입한 회원 X
            model.addAttribute("errorMessage", "아이디가 존재하지 않습니다.");
            return "login_form";

        }else if(password.equals(pw)){//비밀번호 일치
            //세션에 사용자 id 저장
            session.setAttribute("memberId",id);
            return "redirect:/";
            
        }else{//비밀번호 불일치
            model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "login_form";

        }
        
    }
    
    //로그아웃
    @PostMapping("/login/logout")
    public String userLogout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }
}
