package com.sm.controller;

import com.sm.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.TreeMap;

/**
 * 主页控制器
 */
@Controller
@RequestMapping("/home")
public class HomeController {
    private static final int FLAG_FOLLOW = 1;
    private static final int FLAG_STRANGE = 2;
    private static final int FLAG_FANS = 3;
    private static final int FLAG_FANS2 = 4;

    private final RelationService service;

    @Autowired
    public HomeController(RelationService service) {
        this.service = service;
    }

    @GetMapping("/unfollow/{myId}/{followId}")
    public String doUnFollow(@PathVariable String myId, @PathVariable String followId, Model model, RedirectAttributes att) {
        boolean c = service.doUnFollow(myId, followId);
        if (c) {
            att.addAttribute("myId", myId);
            model.addAttribute("checkMsg", "取消关注成功");
        }
        return "redirect:/home/getFollow";
    }

    @GetMapping("/follow/{myId}/{followId}/{flag}")
    public String doFollow(@PathVariable String myId, @PathVariable String followId, @PathVariable String flag, Model model, RedirectAttributes att) {
        boolean c = service.doFollow(myId, followId);
        if (c) {
            att.addAttribute("myId", myId);
            model.addAttribute("checkMsg", "关注成功");
        }
        if (flag.equals("2")) {
            return "redirect:/home/getStrange";
        }
        return "redirect:/home/getFans";
    }

    @GetMapping("/getFans")
    public String getFans(String myId, Model model) {
        //TreeMap<String, String> map = service.getFans(myId);
        TreeMap<String, String> map = service.getFansNoFollow(myId);
        TreeMap<String, String> followBackMap = service.followBackMap(myId);
        TreeMap<String, String> infoMap = service.getUserBaseInfo(myId);
        model.addAttribute("infoMap", infoMap);
        model.addAttribute("map", map);
        model.addAttribute("followBackMap", followBackMap);
        model.addAttribute("flag", FLAG_FANS);
        return "home";
    }

    @GetMapping("/getFollow")
    public String getFollow(String myId, Model model) {
        //TreeMap<String, String> map = service.getFollow(myId);
        TreeMap<String, String> map = service.getFollowNoFans(myId);
        TreeMap<String, String> followBackMap = service.followBackMap(myId);
        TreeMap<String, String> infoMap = service.getUserBaseInfo(myId);
        model.addAttribute("infoMap", infoMap);
        model.addAttribute("map", map);
        model.addAttribute("followBackMap", followBackMap);
        model.addAttribute("flag", FLAG_FOLLOW);
        return "home";
    }

    @GetMapping("/getStrange")
    public String getStrange(String myId, Model model) {
        TreeMap<String, String> map = service.getStranger(myId);
        map = service.getMapPage(map, 1);
        TreeMap<String, String> infoMap = service.getUserBaseInfo(myId);
        model.addAttribute("infoMap", infoMap);
        model.addAttribute("map", map);
        model.addAttribute("flag", FLAG_STRANGE);
        return "home";
    }


    @PostMapping("/AreYouAFan")
    public String AreYouAFan(String myId, String fanName, Model model) {
        TreeMap<String, String> map = service.AreYouAFan(myId, fanName);
        TreeMap<String, String> infoMap = service.getUserBaseInfo(myId);
        model.addAttribute("infoMap", infoMap);
        model.addAttribute("map", map);
        model.addAttribute("flag", FLAG_FANS2);
        return "home";
    }
}
