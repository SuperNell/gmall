package com.atguigu.gmall.passprot.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.UserInfo;
import com.atguigu.gmall.passprot.util.JwtUtil;
import com.atguigu.gmall.service.UserInfoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PassportController {

    @Value("${token.key}")
    String signKey;

    @Reference
    private UserInfoService userInfoService;

    @RequestMapping("index")
    public String index(HttpServletRequest request) {
        String originUrl = request.getParameter("originUrl");
        request.setAttribute("originUrl", originUrl);
        return "index";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public String login(HttpServletRequest request, UserInfo userInfo) {
        String remoteAddress = request.getHeader("X-forwarded-for");
        if (userInfo != null) {
            UserInfo userLogin = userInfoService.login(userInfo);
            if (userLogin != null) {
                HashMap<String, Object> map = new HashMap<>();
                String id = userLogin.getId();
                String nickName = userLogin.getNickName();
                map.put("userId", id);
                map.put("nickName", nickName);
                String token = JwtUtil.encode(signKey, map, remoteAddress);
                System.out.println("token=" + token);
                return token;
            }
        }
        return "fail";
    }

    @RequestMapping("verify")
    @ResponseBody
    public String verify(HttpServletRequest request) {
        String token = request.getParameter("token");
        String currentIp = request.getParameter("currentIp");
        Map<String, Object> map = JwtUtil.decode(token, signKey, currentIp);
        if (map != null) {
            String userId = (String) map.get("userId");
            UserInfo userInfo = userInfoService.verify(userId);
            if (userInfo != null) {
                return "success";
            }
        }
        return "fail";
    }
}
