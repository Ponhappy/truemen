package com.truemen.api.guide.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.truemen.api.guide.model.Guide;
import com.truemen.api.guide.service.GuideService;

@RestController
@RequestMapping("/api/v1/guides")
public class GuideController {

    @Autowired
    private GuideService guideService;

    // 查看某个具体攻略
    @GetMapping("/{guideId}")
    public Guide getGuide(@PathVariable("guideId") Long guideId) {
        return guideService.getGuideById(guideId);
    }

    // 编辑某个具体攻略
    @PutMapping("/{id}/edit")
    public Guide updateGuide(@PathVariable Long id, @RequestBody Guide guide) {
        return guideService.updateGuide(id, guide);
    }

    // 创建攻略
    @PostMapping("/create")
    public Guide createGuide(@RequestBody Guide guide) {
        return guideService.createGuide(guide);
    }

    // 点击+按钮后增加一天
    @PostMapping("/{id}/addDay")
    public Guide addDayToGuide(@PathVariable Long id) {
        return guideService.addDayToGuide(id);
    }
}