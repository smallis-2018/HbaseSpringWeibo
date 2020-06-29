package com.sm.controller;

import com.sm.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.TreeMap;

@Controller
public class LoginController {

    private final RelationService service;

    @Autowired
    public LoginController(RelationService service) {
        this.service = service;
    }


    @GetMapping("/login")
    public String login(String myId, Model model) {
        TreeMap<String, String> userBaseInfo = service.getUserBaseInfo(myId);
        String name = "";
        for (String key : userBaseInfo.keySet()) {
            name = userBaseInfo.get(key);
        }
        if (name.equals("")) {
            model.addAttribute("checkMsg", "帐号不存在");
            return "../../index";
        } else {
            model.addAttribute("myId", myId);
            model.addAttribute("myName", name);
            return "redirect:/home/getFans";
        }
    }


}
