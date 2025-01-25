package com.example.demo.controller;

import com.example.demo.model.Item;
import com.example.demo.model.Stock;
import com.example.demo.model.Cart;
import com.example.demo.model.UserInfo;
import com.example.demo.service.ItemService;
import com.example.demo.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.reflect.Member;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    //@Autowired 대신에 새로운 방법 >> @RequiredArgsConstructor + final 조합
    private final ItemService itemService;
    private final MemberService memberService;
    private final RestTemplate restTemplate;

    @Value("${server.url.http}")
    private String serverUrl;


    // 상품 목록 페이지
    @GetMapping("/")
    public String getAllItems(Model model, HttpSession session) {
        List<Item> items = itemService.selectItemList();
        String userId = (String)session.getAttribute("memberId");
        UserInfo userInfo = memberService.getUserInfo(userId);

        model.addAttribute("items", items);
        model.addAttribute("userInfo",userInfo);
        return "item_list";
    }

    // 상품 추가 폼
    @GetMapping("/item/new")
    public String createItemForm(Model model,HttpSession session) {
                //로그인한 사용자 정보 불러오기
        String userId = (String)session.getAttribute("memberId");
        if(userId==null){
            return "redirect:/login";
        }
        UserInfo userInfo = memberService.getUserInfo(userId);
        int grade_cd = userInfo.getGradeCd();

        if(grade_cd!=99){
            return "redirect:/";
        }

        model.addAttribute("item", new Item());
        return "item_form";
    }

    // 상품 저장
    @PostMapping("/item/saveItem")
    @Transactional(rollbackFor = Exception.class)
    public String saveItemItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        try{
            itemService.saveItem(item);

            //8081 서버로 상품 id와 stock 전송
            String apiUrl = serverUrl+"/getItemStock";
            Stock stock = new Stock(item.getId(),item.getStock());
            restTemplate.postForEntity(apiUrl, stock, Void.class);


        }catch (Exception e){
            log.error("재고 서버에 연결할 수 없습니다. 원인: {}", e.getMessage(), e);
            // 예외 발생 시 메시지 추가
            redirectAttributes.addFlashAttribute("alertMessage", "재고 서버에 연결할 수 없습니다.");

            throw new RuntimeException("Stock server connection failed", e);
        }

        return "redirect:/";
    }

    @ExceptionHandler(RuntimeException.class)
    public String runtimeExceptionHandler(RuntimeException ex,RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("alertMessage", "재고 서버에 연결할 수 없습니다.");
        return "redirect:/errorPage";
    }



    //에러 페이지
    @GetMapping("/errorPage")
    public String errorPage(Model model){
        return "error_page";
    }
}
