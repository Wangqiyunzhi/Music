package com.rabbiter.music.controller;

import com.rabbiter.music.pojo.Comment;
import com.rabbiter.music.service.CommentService;
import com.rabbiter.music.utils.Consts;
import com.rabbiter.music.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * ?
     * serId, type(0/1), content
     * type=0 ?songId
     * type=1 ?songListId
     */
    @PostMapping("/add")
    public R add(Comment comment) {
        if (comment == null || comment.getUserId() == null
                || comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            return R.error(Consts.ERROR);
        }

        int type = (comment.getType() == null) ? 0 : comment.getType().intValue();
        if (type == 0 && comment.getSongId() == null) return R.error(Consts.ERROR);
        if (type == 1 && comment.getSongListId() == null) return R.error(Consts.ERROR);

        comment.setUp(0);
        boolean ok = commentService.insert(comment);
        return ok ? R.success(Consts.SUCCESS, 1) : R.error(Consts.ERROR);
    }

    /**
     * ?
     * /comment/page?type=0&songId=1&pageNo=1&pageSize=10
     * /comment/page?type=1&songListId=2&pageNo=1&pageSize=10
     */
    @GetMapping("/page")
    public R page(@RequestParam Integer type,
                  @RequestParam(required = false) Integer songId,
                  @RequestParam(required = false) Integer songListId,
                  @RequestParam(defaultValue = "1") Integer pageNo,
                  @RequestParam(defaultValue = "10") Integer pageSize) {

        int t = (type == null) ? 0 : type;

        if (t == 0 && songId == null) return R.error("songId");
        if (t == 1 && songListId == null) return R.error("songListId");

        int total = commentService.countByTarget(t, songId, songListId);
        List<Comment> list = commentService.selectPage(t, songId, songListId, pageNo, pageSize);

        // ?{code,msg,total,pageNo,pageSize,data}
        R r = R.success(Consts.SUCCESS, 1);
        r.put("total", total);
        r.put("pageNo", pageNo);
        r.put("pageSize", pageSize);
        r.put("data", list);
        return r;
    }

    /**
     * ??userId?
     * POST /comment/delete   id=xx&userId=xx
     */
    @PostMapping("/delete")
    public R delete(Comment comment) {
        if (comment == null || comment.getId() == null || comment.getUserId() == null) {
            return R.error(Consts.ERROR);
        }
        boolean ok = commentService.deleteByIdAndUserId(comment.getId(), comment.getUserId());
        return ok ? R.success(Consts.SUCCESS, 1) : R.error(Consts.ERROR);
    }
}


