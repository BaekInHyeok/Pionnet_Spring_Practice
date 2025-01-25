package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.model.Item;
import com.example.demo.model.UserInfo;
import com.example.demo.service.CartService;
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

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CartController {

    //@Autowired 대신에 새로운 방법 >> @RequiredArgsConstructor + final 조합
    private final ItemService itemService;
    private final CartService cartService;
    private final MemberService memberService;
    private final RestTemplate restTemplate;

    @Value("${server.url.http}")
    private String serverUrl;

    // 장바구니 페이지
    @GetMapping("/cart")
    public String getAllCarts(Model model, HttpSession session, RedirectAttributes redirectAttributes){
        //로그인한 사용자 정보 불러오기
        String userId = (String)session.getAttribute("memberId");
        if(userId==null){
            return "redirect:/login";
        }
        UserInfo userInfo = memberService.getUserInfo(userId);
        Long member_no = userInfo.getMemberNo();

        List<Cart> cartItems = cartService.selectCartList(member_no);

        //장바구니에 상품이 하나도 없는 경우
        if(cartItems == null || cartItems.isEmpty()){
            redirectAttributes.addFlashAttribute("alertMessage","장바구니에 넣은 상품이 없습니다!");
            //상품이 없다고 alert을 띄우고 메인페이지로 돌아가기
            return "redirect:/emptyCart";
        }

        //8081 서버에서 상품id로 재고량을 가져오는 과정을 반복
        String apiUrl = serverUrl+"/sendItemStock/";
        for(Cart cart : cartItems){
            Long itemId = cart.getItem_id();
            String postURL = apiUrl+itemId;
            try{
                Integer stock = restTemplate.getForObject(postURL,Integer.class);
                if (stock != null) {
                    cart.getItem().setStock(stock);
                } else {
                    cart.getItem().setStock(0); //재고정보가 없으면 기본값으로 0
                    log.warn("No stock info returned for item ID: {}", itemId);
                }
            }catch (Exception e) {
                log.error("재고 서버에 연결할 수 없습니다. 원인: {}", e.getMessage(), e);
                // 예외 발생 시 메시지 추가
                redirectAttributes.addFlashAttribute("alertMessage", "재고 서버에 연결할 수 없습니다.");

                throw new RuntimeException();
            }
        }

        model.addAttribute("cartItems",cartItems);
        return "cart_list";
    }

    // 장바구니에 상품 추가
    @PostMapping("/cart/add/{id}")
    @Transactional(rollbackFor = Exception.class)
    public String addItemCart(@PathVariable("id")Long id, HttpSession session,RedirectAttributes redirectAttributes){

        String apiUrl = serverUrl;
        RestTemplate restTemplate = new RestTemplate();

        try{
            restTemplate.getForEntity(apiUrl,Void.class);

            //로그인한 사용자 정보 불러오기
            String userId = (String)session.getAttribute("memberId");
            if(userId==null){
                return "redirect:/login";
            }

            UserInfo userInfo = memberService.getUserInfo(userId);
            Long member_no = userInfo.getMemberNo();

            //장바구니 목록에 상품이 이미 존재하는지 먼저 확인한다.
            //존재하면 장바구니 객체 반환, 없으면 null
            Cart cartItem =cartService.searchItem(id,member_no);
            if(cartItem != null){
                cartItem.setQuantity(cartItem.getQuantity()+1);
                cartService.updateCart(cartItem);
            }else{
                Cart newCartItem = new Cart();
                newCartItem.setItem_id(id);
                newCartItem.setQuantity(1);
                newCartItem.setMember_no(member_no);
                newCartItem.setCreated_at(LocalDateTime.now());

                cartService.addToCart(newCartItem);
            }
        }catch (Exception e){
            log.error("재고 서버에 연결할 수 없습니다. 원인: {}", e.getMessage(), e);
            // 예외 발생 시 메시지 추가
            redirectAttributes.addFlashAttribute("alertMessage", "재고 서버에 연결할 수 없습니다.");

            throw new RuntimeException("Stock server connection failed", e);
        }

        return "redirect:/cart";
    }

    @ExceptionHandler(RuntimeException.class)
    public String runtimeExceptionHandler(RuntimeException ex,RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("alertMessage", "재고 서버에 연결할 수 없습니다.");
        return "redirect:/errorPage";
    }
    
    //장바구니 수량 변경
    @PostMapping("/cart/updateQuantity/{cartId}")
    public String updateCartQuantity(@PathVariable("cartId")Long cartId, @RequestParam("quantity") int quantity){
        Cart cart = cartService.searchCart(cartId);

        cart.setQuantity(quantity);

        cartService.updateCart(cart);
        return "redirect:/cart";
    }
    
    //장바구니 항목 삭제
    @PostMapping("/cart/remove/{cartId}")
    public String deleteCart(@PathVariable("cartId")Long cartId){
        cartService.deleteCart(cartId);

        return "redirect:/cart";
    }

    //장바구니가 비었을 때 가는 페이지
    @GetMapping("/emptyCart")
    public String emptyCart(){
        return "empty_cart";
    }

}
