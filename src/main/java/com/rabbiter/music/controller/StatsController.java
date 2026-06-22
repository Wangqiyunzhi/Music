package com.rabbiter.music.controller;

import com.rabbiter.music.service.StatsService;
import com.rabbiter.music.utils.Consts;
import com.rabbiter.music.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/stats", "/api/stats"})
@CrossOrigin
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping("/hot/songs")
    public R hotSongs(@RequestParam(defaultValue = "10") Integer limit) {
        return R.success(Consts.SUCCESS, 1, statsService.hotSongs(limit));
    }

    @GetMapping("/hot/singers")
    public R hotSingers(@RequestParam(defaultValue = "10") Integer limit) {
        return R.success(Consts.SUCCESS, 1, statsService.hotSingers(limit));
    }

    @GetMapping("/rank/play")
    public R playRank(@RequestParam(defaultValue = "10") Integer limit) {
        return R.success(Consts.SUCCESS, 1, statsService.playRank(limit));
    }

    @GetMapping("/rank/collect")
    public R collectRank(@RequestParam(defaultValue = "10") Integer limit) {
        return R.success(Consts.SUCCESS, 1, statsService.collectRank(limit));
    }

    @GetMapping("/user/daily")
    public R dailyNewUsers(@RequestParam(defaultValue = "14") Integer days) {
        return R.success(Consts.SUCCESS, 1, statsService.dailyNewUsers(days));
    }
}


