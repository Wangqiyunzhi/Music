package com.rabbiter.music.controller;

import com.alibaba.fastjson.JSONObject;
import com.rabbiter.music.pojo.Rank;
import com.rabbiter.music.service.RankService;
import com.rabbiter.music.utils.Consts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(tags = "Rank")
public class RankController {

    @Autowired
    private RankService rankService;

    /**
     * Add rank
     */
    @ApiOperation(value = "Add rank")
    @RequestMapping(value = "/rank/add", method = RequestMethod.POST)
    public Object add(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        try {
            String songListId = request.getParameter("songListId");
            String consumerId = request.getParameter("consumerId");
            String score = request.getParameter("score");

            if (songListId == null || consumerId == null || score == null) {
                jsonObject.put(Consts.CODE, 0);
                jsonObject.put(Consts.MSG, "songListId/consumerId/score required");
                return jsonObject;
            }

            Rank rank = new Rank();
            rank.setSongListId(Integer.parseInt(songListId));
            rank.setConsumerId(Integer.parseInt(consumerId));
            rank.setScore(Integer.parseInt(score));

            boolean flag = rankService.insert(rank);
            jsonObject.put(Consts.CODE, flag ? 1 : 0);
            jsonObject.put(Consts.MSG, flag ? "add success" : "add failed");
            return jsonObject;

        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "add failed: " + e.getMessage());
            return jsonObject;
        }
    }

    /**
     * Query rank by songListId
     */
    @ApiOperation(value = "Query rank by songListId")
    @RequestMapping(value = "/rank", method = RequestMethod.GET)
    public Object rankOfSongListId(HttpServletRequest request) {
        String songListId = request.getParameter("songListId");
        return rankService.rankOfSongListId(Integer.parseInt(songListId));
    }
}
