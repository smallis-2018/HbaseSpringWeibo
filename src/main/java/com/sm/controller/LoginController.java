package com.sm.controller;

import com.sm.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.TreeMap;

@Controller
@SessionAttributes(names = {"oid"}, types = {String.class})
public class LoginController {

    private final RelationService service;

    @Autowired
    public LoginController(RelationService service) {
        this.service = service;
    }


    @GetMapping("/login")
    public String login(String myId, Model model, RedirectAttributes att) {
        TreeMap<String, String> infoMap = service.getUserBaseInfo(myId);
        String oid = myId;
        String name = "";
        for (String key : infoMap.keySet()) {
            name = infoMap.get(key);
        }

        if (name.equals("")) {
            model.addAttribute("checkMsg", "帐号不存在");
            return "../../index";
        } else {
            model.addAttribute("oid", oid);
            att.addAttribute("myId", myId);
            return "redirect:/home/getFans";
        }
    }


}
