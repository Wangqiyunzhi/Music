package com.rabbiter.music.controller;

import com.rabbiter.music.pojo.PlayHistory;
import com.rabbiter.music.service.PlayHistoryService;
import com.rabbiter.music.service.SongService;
import com.rabbiter.music.utils.Consts;
import com.rabbiter.music.vo.PlayHistoryItem;
import com.rabbiter.music.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/history")
public class PlayHistoryController {

    @Autowired
    private PlayHistoryService playHistoryService;

    @Autowired
    private SongService songService;

    /**  */
    @PostMapping("/add")
    public R add(@RequestParam Integer userId, @RequestParam Integer songId) {
        try {
            if (userId == null || songId == null) return R.error(Consts.ERROR);

            PlayHistory h = new PlayHistory();
            h.setUserId(userId);
            h.setSongId(songId);

            boolean ok = playHistoryService.insert(h);
            if (ok) {
                songService.incrPlayCount(songId);
                return R.success(Consts.SUCCESS, 1);
            }
            return R.error(Consts.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(Consts.ERROR);
        }
    }

    /**  */
    @GetMapping("/user")
    public List<PlayHistoryItem> historyOfUser(@RequestParam Integer userId,
                                               @RequestParam(defaultValue = "50") Integer limit) {
        if (userId == null) return Collections.emptyList();
        return playHistoryService.historyOfUser(userId, limit);
    }

    /**  */
    @GetMapping("/clear")
    public R clear(@RequestParam Integer userId) {
        try {
            boolean ok = playHistoryService.deleteByUserId(userId);
            return ok ? R.success(Consts.SUCCESS, 1) : R.error(Consts.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(Consts.ERROR);
        }
    }
}


