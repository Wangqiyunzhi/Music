package com.rabbiter.music.controller;

import com.rabbiter.music.pojo.Collect;
import com.rabbiter.music.service.CollectService;
import com.rabbiter.music.utils.Consts;
import com.rabbiter.music.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collect")
public class CollectController {

    @Autowired
    private CollectService collectService;

    /**
     * ?/?
     * serId, type(0/1)
     * type=0 ?songId
     * type=1 ?songListId
     *
     *  code?
     * 1 = 
     * 2 = 
     * 0 = 
     */
    @PostMapping("/add")
    public R add(Collect collect) {

        if (collect == null || collect.getUserId() == null || collect.getType() == null) {
            return R.error(Consts.ERROR);
        }

        int type = collect.getType();

        if (type == 0) {
            if (collect.getSongId() == null) return R.error(Consts.ERROR);

            boolean existed = collectService.existSongId(collect.getUserId(), collect.getSongId());
            if (existed) {
                boolean ok = collectService.deleteByUserIdSongId(collect.getUserId(), collect.getSongId());
                return ok ? R.success(Consts.UNCOLLECT_SONG, 2) : R.error(Consts.ERROR);
            } else {
                boolean ok = collectService.insert(collect);
                return ok ? R.success(Consts.COLLECT_SONG, 1) : R.error(Consts.ERROR);
            }
        }

        if (type == 1) {
            if (collect.getSongListId() == null) return R.error(Consts.ERROR);

            boolean existed = collectService.existSongListId(collect.getUserId(), collect.getSongListId());
            if (existed) {
                boolean ok = collectService.deleteByUserIdSongListId(collect.getUserId(), collect.getSongListId());
                return ok ? R.success(Consts.UNCOLLECT_SONGLIST, 2) : R.error(Consts.ERROR);
            } else {
                boolean ok = collectService.insert(collect);
                return ok ? R.success(Consts.COLLECT_SONGLIST, 1) : R.error(Consts.ERROR);
            }
        }

        return R.error(Consts.ERROR);
    }

    /** ?*/
    @GetMapping("/collectOfUserId")
    public List<Collect> collectOfUserId(@RequestParam Integer userId){
        return collectService.collectOfUserId(userId);
    }

    /** ?0 / 1?*/
    @GetMapping("/collectOfUserIdAndType")
    public List<Collect> collectOfUserIdAndType(@RequestParam Integer userId,
                                                @RequestParam Integer type){
        return collectService.collectOfUserIdAndType(userId, type);
    }
}


