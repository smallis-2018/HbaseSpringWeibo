package com.sm.controller;

import com.sm.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.TreeMap;

/**
 * 其他用户主页控制器
 */
@Controller
@RequestMapping("/other")
public class OtherController {
    private static final int FLAG_FOLLOW = 1;
    private static final int FLAG_STRANGE = 2;
    private static final int FLAG_FANS = 3;

    private final RelationService service;

    @Autowired
    public OtherController(RelationService service) {
        this.service = service;
    }

    @GetMapping("/getFans")
    public String getFans(String myId, Model model) {
        TreeMap<String, String> map = service.getFans(myId);
        TreeMap<String, String> infoMap = service.getUserBaseInfo(myId);
        model.addAttribute("infoMap", infoMap);
        model.addAttribute("map", map);
        model.addAttribute("flag", FLAG_FANS);
        return "other";
    }

    @GetMapping("/getFollow")
    public String getFollow(String myId, Model model) {
        TreeMap<String, String> map = service.getFollow(myId);
        TreeMap<String, String> infoMap = service.getUserBaseInfo(myId);
        model.addAttribute("infoMap", infoMap);
        model.addAttribute("map", map);
        model.addAttribute("flag", FLAG_FOLLOW);
        return "other";
    }

    @GetMapping("/getStrange")
    public String getStrange(String myId, Model model) {
        TreeMap<String, String> map = service.getStranger(myId);
        map = service.getMapPage(map, 1);
        TreeMap<String, String> infoMap = service.getUserBaseInfo(myId);
        model.addAttribute("infoMap", infoMap);
        model.addAttribute("map", map);
        model.addAttribute("flag", FLAG_STRANGE);
        return "other";
    }

    @PostMapping("/AreYouAFan")
    public String AreYouAFan(String myId, String fanName, Model model) {
        TreeMap<String, String> map = service.AreYouAFan(myId, fanName);
        TreeMap<String, String> infoMap = service.getUserBaseInfo(myId);
        model.addAttribute("infoMap", infoMap);
        model.addAttribute("map", map);
        model.addAttribute("flag", FLAG_FANS);
        return "other";
    }
}
