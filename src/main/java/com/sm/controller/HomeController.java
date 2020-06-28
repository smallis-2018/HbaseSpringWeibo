package com.sm.controller;

import com.sm.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.TreeMap;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final RelationService service;

    @Autowired
    public HomeController(RelationService service) {
        this.service = service;
    }

    @GetMapping("/getMap")
    public String getMap(String myId, Model model) {
        if (myId.equals("")) {
            myId = (String) model.getAttribute(myId);
        }
        TreeMap<String, String> infoMap = service.getUserBaseInfo(myId);
        TreeMap<String, String> followMap = service.getFollow(myId);
        TreeMap<String, String> fansMap = service.getFans(myId);
        TreeMap<String, String> strangerMap = service.getStranger(myId);
        model.addAttribute("infoMap", infoMap);
        model.addAttribute("followMap", followMap);
        model.addAttribute("fansMap", fansMap);
        model.addAttribute("strangerMap", strangerMap);
        return "home";
    }

    @GetMapping("/unfollow/{myId}/{followId}")
    public String doUnFollow(@PathVariable String myId, @PathVariable String followId, Model model) {
        boolean c = service.doUnFollow(myId, followId);
        if (c) {
            model.addAttribute("checkMsg", "已取消关注");
        } else {
            model.addAttribute("checkMsg", "操作失败");
        }
        model.addAttribute("myId", myId);
        return "redirect:/home/getMap";
    }

    @GetMapping("/follow/{myId}/{followId}")
    public String doFollow(@PathVariable String myId, @PathVariable String followId, Model model) {
        boolean c = service.doFollow(myId, followId);
        if (c) {
            model.addAttribute("checkMsg", "关注成功啦");
        } else {
            model.addAttribute("checkMsg", "操作失败");
        }
        model.addAttribute("myId", myId);
        return "redirect:/home/getMap";
    }

}
