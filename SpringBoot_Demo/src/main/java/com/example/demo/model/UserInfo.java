package com.example.demo.model;

import lombok.Data;

@Data
public class UserInfo {
    public final String sessionId;
    public Long memberNo;
    public int gradeCd;
    public String memberId;
}
